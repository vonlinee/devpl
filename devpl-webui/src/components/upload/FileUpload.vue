<template>
    <div class="upload-file pms-upload-file">
        <el-upload
            multiple
            :action="uploadFileUrl"
            :before-upload="handleBeforeUpload"
            :file-list="fileList"
            :limit="limit"
            :on-error="handleUploadError"
            :on-exceed="handleExceed"
            :on-success="handleUploadSuccess"
            :show-file-list="false"
            :headers="headers"
            class="upload-file-uploader"
            :auto-upload="autoUpload"
            :on-change="loadJsonFromFile"
            :data="uploadData"
            :timeout="60000"
            ref="fileUpload"
        >
            <!-- 上传按钮 -->
            <slot name="btn">
                <el-button size="small" type="primary">上传附件</el-button>
            </slot>
            <!-- 上传提示 -->
            <div
                class="el-upload__tip"
                slot="tip"
                v-if="showTip">
                请上传
                <template v-if="fileSize">
                    大小不超过
                    <b style="color: #f56c6c">{{ fileSize }}MB</b>
                </template>
                <template v-if="fileType">
                    格式为
                    <b style="color: #f56c6c">{{ fileType.join('/') }}</b>
                </template>
                的文件
            </div>
        </el-upload>

        <!-- 文件列表 -->
        <transition-group
            v-if="showFileList"
            class="upload-file-list el-upload-list el-upload-list--text"
            name="el-fade-in-linear"
            tag="ul"
        >
            <li
                :key="file.url"
                class="el-upload-list__item ele-upload-list__item-content"
                v-for="(file, index) in fileList"
            >
        <span class="el-icon-document ml-5 mr-5">
          {{ getFileName(file.name) }}
        </span>
                <div class="ele-upload-list__item-content-action pr-5">
                    <el-button
                        type="text"
                        @click="previewFile(file)"
                    >
                        预览
                    </el-button>

                    <el-button
                        type="text"
                        @click="downFile(file)"
                    >
                        下载
                    </el-button>
                    <el-button
                        v-if="showDelButton"
                        type="text"
                        @click="handleDelete(index)"
                    >
                        删除
                    </el-button>
                </div>
            </li>
        </transition-group>
    </div>
</template>

<script>
import {randomNum} from '@/utils/auth';
import store from '@/store';
import {ElButton} from "element-plus";

export default {
    name: 'FileUpload',
    components: {ElButton},
    props: {
        // 值
        value: [String, Object, Array],
        // 数量限制
        limit: {
            type: Number,
            default: 5
        },
        // 大小限制(MB)
        fileSize: {
            type: Number,
            default: 5
        },
        // 文件类型, 例如['png', 'jpg', 'jpeg']
        fileType: {
            type: Array,
            default: () => [
                'doc',
                'docx',
                'xls',
                'xlsx',
                'ppt',
                'pptx',
                'txt',
                'pdf',
                'jpg',
                'png',
                'jpeg',
                'zip'
            ]
        },
        // 是否显示提示
        isShowTip: {
            type: Boolean,
            default: true
        },
        // 是否在选取文件后立即进行上传
        autoUpload: {
            type: Boolean,
            default: true
        },
        // 是否显示已上传文件列表
        showFileList: {
            type: Boolean,
            default: true
        },
        //是否显示删除按钮
        showDelButton: {
            type: Boolean,
            default: true
        },
        // 是否在input事件回调中返回url字符串拼接
        isReturnUrlStr: {
            type: Boolean,
            default: false
        },
        fileUrl: {
            type: String,
            default: '/bom/file/file/upload' // 默认上传接口
        },
        // 上传文件的额外参数
        uploadData: {
            type: Object,
            default: () => {
                return {};
            }
        }
    },
    data() {
        return {
            number: 0,
            uploadList: [],
            baseUrl: process.env.VUE_APP_BASE_API,
            uploadFileUrl: '',
            // uploadFileUrl: process.env.VUE_APP_BASE_API + '/common/upload', // 上传文件服务器地址
            headers: {
                // 'content-type': 'multipart/form-data;'
            },
            fileList: []
        };
    },
    created() {
        // 拼接文件上传地址
        this.uploadFileUrl = process.env.VUE_APP_BASE_API + this.fileUrl;
        // 设置请求头
        this.setHeaders();
    },
    // computed: {
    //   uploadFileUrl() {
    //     return process.env.VUE_APP_BASE_API + this.fileUrl;
    //   }
    // },
    watch: {
        value: {
            handler(val) {
                if (val) {
                    let temp = 1;
                    // 首先将值转为数组
                    const list = Array.isArray(val) ? val : this.value.split(',');
                    // 然后将数组转为对象数组
                    this.fileList = list.map((item) => {
                        if (typeof item === 'string') {
                            item = {name: item, url: item};
                        }
                        item.uid = item.uid || new Date().getTime() + temp++;
                        return item;
                    });
                } else {
                    this.fileList = [];
                    return [];
                }
            },
            deep: true,
            immediate: true
        }
    },
    computed: {
        // 是否显示提示
        showTip() {
            return this.isShowTip && (this.fileType || this.fileSize);
        }
    },
    methods: {
        // 文件预览
        async previewFile(file) {
            if (!file.fileToken) {
                this.$message.error('缺少文件fileToken');
                return;
            }
            this.$fileFn.previewFile(file.fileName, file.fileToken, file.fileId);
        },
        // 文件下载
        async downFile(file) {
            this.$emit('downFile', file);
        },
        // 设置请求头
        setHeaders() {
            // 上传请求头设置
            const headers = {
                appid: 'deespc',
                nonceStr: randomNum(),
                timestamp: Date.now()
            };

            if (store.getters.token) {
                // 登录的token
                headers.Authorization = ''
            }
            this.headers = headers;
        },
        // 设置请求头的 sign 参数
        setHeaderSign(file) {
            const data = {...this.uploadData};
            const form = new FormData();
            for (const key in data) {
                form.append(key, data[key]);
            }
            form.append('file', file);
            this.headers.sign = xxx
        },
        //  文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
        loadJsonFromFile(file, fileList) {
            // 当上传交互为手动触发再去上传时，通过该钩子获取file文件对象
            if (!this.autoUpload && this.showFileList) {
                console.log(fileList);

                this.fileList = fileList;
            }
        },
        onRemove(file, fileList) {
            // 只有手动触发时再去删除
            if (!this.autoUpload && this.showFileList) {
                const index = this.fileList.findIndex(
                    (fileItem) => fileItem.uid === file.uid
                );
                this.fileList.splice(index, 1);
            }
        },
        // 上传前校检格式和大小
        handleBeforeUpload(file) {
            // 校检文件类型
            if (this.fileType) {
                const fileName = file.name.split('.');
                const fileExt = fileName[fileName.length - 1];
                const isTypeOk = this.fileType.indexOf(fileExt) >= 0;
                if (!isTypeOk) {
                    this.$modal.msgError(
                        `文件格式不正确, 请上传${this.fileType.join('/')}格式文件!`
                    );
                    return false;
                }
            }
            // 校检文件大小
            if (this.fileSize) {
                const isLt = file.size / 1024 / 1024 < this.fileSize;
                if (!isLt) {
                    this.$modal.msgError(`上传文件大小不能超过 ${this.fileSize} MB!`);
                    return false;
                }
            }

            // 设置请求头 sing 参数
            this.setHeaderSign(file);

            this.$modal.loading('正在上传文件，请稍候...');
            this.number++;
            return true;
        },
        // 文件个数超出
        handleExceed() {
            this.$modal.msgError(`上传文件数量不能超过 ${this.limit} 个!`);
        },
        // 上传失败
        handleUploadError(err) {
            this.$modal.msgError('上传文件失败，请重试');
            this.$modal.closeLoading();
        },
        // 上传成功回调
        handleUploadSuccess(res, file) {
            console.log(res, file);
            if (res.code === 1) {
                this.uploadList.push({

                });
                this.uploadedSuccessfully();
            } else {
                this.number--;
                this.$modal.closeLoading();
                this.$modal.msgError(res.msg);
                this.$refs.fileUpload.handleRemove(file);
                this.uploadedSuccessfully();
            }
        },
        // 删除文件
        handleDelete(index) {
            this.fileList.splice(index, 1);
            this.$emit('input', this.listToString(this.fileList));
        },
        // 上传结束处理
        uploadedSuccessfully() {
            if (this.number > 0 && this.uploadList.length === this.number) {
                this.fileList = this.fileList.concat(this.uploadList);
                this.uploadList = [];
                this.number = 0;
                this.$emit('input', this.listToString(this.fileList));
                this.$modal.closeLoading();
            }
        },
        // 获取文件名称
        getFileName(name) {
            if (name.lastIndexOf('/') > -1) {
                return name.slice(name.lastIndexOf('/') + 1);
            } else {
                return name;
            }
        },
        // 对象转成指定字符串分隔
        listToString(list, separator) {
            if (!this.isReturnUrlStr) {
                return list;
            }
            let strs = '';
            separator = separator || ',';
            for (let i in list) {
                strs += list[i].url + separator;
            }
            return strs !== '' ? strs.substr(0, strs.length - 1) : '';
        }
    }
};
</script>

