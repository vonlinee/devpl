<template>
    <el-card>
        <el-form :inline="true" :model="state.queryForm" @keyup.enter="getDataList()">
                        <el-form-item>
                <el-button @click="getDataList()">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button v-auth="'maku:spec:save'" type="primary" @click="addOrUpdateHandle()">
                    新增
                </el-button>
            </el-form-item>
            <el-form-item>
                <el-button v-auth="'maku:spec:delete'" type="danger" @click="deleteBatchHandle()">
                    删除
                </el-button>
            </el-form-item>
        </el-form>
        <el-table v-loading="state.dataListLoading" :data="state.dataList" border style="width: 100%"
                  @selection-change="selectionChangeHandle">
            <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
            <el-table-column label="操作" fixed="right" header-align="center" align="center" width="150">
                <template #default="scope">
                    <el-button v-auth="'maku:spec:update'" type="primary" link
                               @click="addOrUpdateHandle(scope.row.id)">修改
                    </el-button>
                    <el-button v-auth="'maku:spec:delete'" type="primary" link
                               @click="deleteBatchHandle(scope.row.id)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            :current-page="state.page"
            :page-sizes="state.pageSizes"
            :page-size="state.limit"
            :total="state.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="sizeChangeHandle"
            @current-change="currentChangeHandle"
        >
        </el-pagination>

        <!-- 弹窗, 新增 / 修改 -->
        <add-or-update ref="addOrUpdateRef" @refreshDataList="getDataList"></add-or-update>
    </el-card>
</template>

<script setup lang="ts" name="MakuSpecIndex">
    import {useCrud} from '@/hooks'
    import {reactive, ref} from 'vue'
    import {IHooksOptions} from '@/hooks/interface'

    const state: IHooksOptions = reactive({
        dataListUrl: '/maku/spec/page',
        deleteUrl: '/maku/spec',
        queryForm: {
        }
    })

    const addOrUpdateRef = ref()
    const addOrUpdateHandle = (id
    ? : number
    ) =>
    {
        addOrUpdateRef.value.init(id)
    }

    const {
        getDataList,
        selectionChangeHandle,
        sizeChangeHandle,
        currentChangeHandle,
        deleteBatchHandle
    } = useCrud(state)
</script>
