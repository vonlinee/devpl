<template>
    <el-table stripe v-loading="loading" :data="classificationList" style="width: 100%"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" ref="sortableTable" :key="tableKey" row-key="id"
        @row-click="rowClick">
        <el-table-column type="index" width="50" label="序号"></el-table-column>
        <el-table-column prop="name" show-overflow-tooltip label="分类名称"></el-table-column>
        <el-table-column prop="code" show-overflow-tooltip label="分类编码"></el-table-column>
        <el-table-column prop="operating" label="操作" width="220">
            <template slot-scope="scope">
                <el-button type="text" @click="getDetailCategory(scope.row)">详情</el-button>
                <el-button type="text" @click="getEditCategory(scope.row)">编辑</el-button>
                <el-button type="text" @click="getDeleteC(scope.row.id)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>
</template>

<script lang="js">

import Sortable from 'sortablejs'

export default {
    name: "DraggableTable",
    data() {
        return {
            isAddShopCategory: false,
            isDetails: false,
            isEdit: false,
            activeName: 'first',
            details: {},
            classificationList: [
                {
                    id: 1,
                    name: "一级A",
                    code: "一级A",
                    children: [{
                        id: 11,
                        name: "二级",
                        code: "A2",
                    }]
                },
                {
                    id: 2,
                    name: "一级B",
                    code: "一级B",
                    children: [{
                        id: 21,
                        name: "二级B",
                        code: "B1",
                    }]
                }
            ],
            commodityList: [],
            ids: { id: '', parentId: '', shopCategoryId: '', },
            loading: false,
            ownerNameList: [],
            payeeUuidList: [],
            invoiceDrawerUuidList: [],
            tableData: [],
            pageInfo: {},
            total: 0,
            pageNum: 1,
            pageSize: 10,
            sortable: null,  // sortable对象
            activeRows: new Array(0),  // 排序时用来跟后端进行数据交互
            tableKey: ''  // 这个蛮重要的，让dom刷新

        }
    },
    created() {
        this.ids.id = this.$route.query.id
        if (this.ids.id) {
            this.getDetail(this.ids.id);
            this.getCategoryTree(this.ids.id)
        }
    },
    methods: {
        getCategoryTree(id) {
            shopCategoryTree({ id: id }).then(res => {
                let arr = this.sortMenu(res.data)
                this.classificationList = arr
                //初始化数据
                this.initData()
            })
        },
        //根据后台的sort递归字段排序
        sortMenu(arr) {
            arr.forEach((item) => {
                if (item.children && item.children.length) {
                    this.sortMenu(item.children);
                }
            })
            arr.sort((a, b) => {
                return a.sort - b.sort;
            })
            return arr;
        },
        async initData() {
            if (this.sortable && this.sortable.el) {
                this.sortable.destroy()
            }
            // 这里是把树结构的数据再按照从上到下的顺序转化成平级数据
            this.arrayFlagLevel(this.classificationList, 1)
            // 这里是把树结构的数据再按照从上到下的顺序转化成平级数据
            this.$set(this, 'activeRows', this.treeToTile(this.classificationList))
            // 这里是给tableKey一个随机数，Math.random也可以的，只要跟上一次的值不一样，能让dom刷新就可以
            this.tableKey = new Date().getTime()
            this.$nextTick(() => {
                this.setSort()
            })
        },
        setSort() {
            const el = document.querySelectorAll('table.el-table__body > tbody')[0]
            if (!el) {
                return
            }
            console.log("初始化表格拖拽");
            let that = this
            this.sortable = Sortable.create(el, {
                ghostClass: 'sortable-ghost',
                setData: function (dataTransfer) {
                    dataTransfer.setData('Text', '')
                },
                // 拖拽移动的时候
                onMove: function ({ dragged, related }) {
                    //树数据中不管是哪一层，都一定要有level字段
                    /*
                      evt.dragged; // 被拖拽的对象
                      evt.related; // 被替换的对象
                     */
                    const oldRow = that.activeRows[dragged.rowIndex]
                    const newRow = that.activeRows[related.rowIndex]
                    if (oldRow.level !== newRow.level || oldRow.parentId !== newRow.parentId) {
                        return false
                    }
                },
                onEnd: async ({ oldIndex, newIndex }) => {
                    const oldRow = that.activeRows[oldIndex]
                    const newRow = that.activeRows[newIndex]
                    if (oldIndex !== newIndex && oldRow.level === newRow.level && oldRow.parentId === newRow.parentId) {
                        const oldRow = that.activeRows[oldIndex]
                        const newRow = that.activeRows[newIndex]
                        let oldRowSuffixData = that.activeRows.slice(oldIndex)
                        let newRowSuffixData = that.activeRows.slice(newIndex)
                        oldRowSuffixData = oldRowSuffixData.filter((d, i) => i < that.getLeastIndex(oldRowSuffixData.findIndex((_d, _i) => _d.level === oldRow.level && _i !== 0)))
                        newRowSuffixData = newRowSuffixData.filter((d, i) => i < that.getLeastIndex(newRowSuffixData.findIndex((_d, _i) => _d.level === newRow.level && _i !== 0)))
                        const targetRows = that.activeRows.splice(oldIndex, oldRowSuffixData.length)
                        if (oldIndex > newIndex) {
                            that.activeRows.splice(newIndex, 0, ...targetRows)
                        } else if (oldIndex < newIndex) {
                            that.activeRows.splice(newIndex + newRowSuffixData.length - oldRowSuffixData.length, 0, ...targetRows)
                        }
                        let newChildrenId = []
                        let childrenId = []
                        that.activeRows.map(item => {
                            if (oldRow.parentId && newRow.parentId) {
                                if (oldRow.parentId == item.id && newRow.parentId == item.id) {
                                    item.children.forEach(item2 => {
                                        childrenId.push(item2.id)
                                    })
                                }
                                let sortId = that.activeRows.map(d => d.id)
                                newChildrenId = sortId.filter(function (n) {
                                    return childrenId.indexOf(n) != -1
                                });
                            } else if (!oldRow.parentId && !newRow.parentId) {
                                if (oldRow.level == 1 && newRow.level == 1) {
                                    if (oldRow.level == item.level) {
                                        newChildrenId.push(item.id)
                                    }
                                }
                            }
                        })
                        await that.sort(newChildrenId)
                        this.tableKey = new Date().getTime()
                        that.initData();
                    }
                },
            })
        },
        //排序接口
        sort(arr) {
            shopCategorySort({ sortedCategoryId: arr }).then(res => {
                this.$message({
                    type: 'success',
                    message: res.msg
                })
                this.getCategoryTree(this.ids.id)
            })
        },
        getLeastIndex(index) {
            return index >= 1 ? index : 1
        },
        // 将树数据转化为平铺数据
        treeToTile(classificationList, childKey = 'children') {
            let arr = []
            let expanded = data => {
                if (data && data.length > 0) {
                    data.filter(d => d).forEach(e => {
                        arr.push(e)
                        expanded(e[childKey] || [])
                    })
                }
            }
            expanded(classificationList)
            return arr
        },
        //递归给每一级增加一个level
        arrayFlagLevel(array, level) {
            if (!array || !array.length) return;
            array.forEach(item => {
                item.level = level;
                if (item.children && item.children.length) {
                    this.arrayFlagLevel(item.children, level + 1);
                }
            })
        }
    }
}

</script>