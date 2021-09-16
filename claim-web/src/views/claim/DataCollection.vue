<template>
	<section>
		<el-card shadow="hover" style="line-height: 40px;">
			<el-descriptions class="margin-top" title="资料收集"></el-descriptions>
			<el-row>
				<el-button type="primary" style="float: left;" size="small" @click="selectCollection" :disabled="casedis">单证选择</el-button>
			</el-row>
			
			<el-table :data="tableData" highlight-current-row v-loading="loading" style="width: 100%; margin-top: 10px;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column prop="certifyName" label="单证类型" width="200">
				</el-table-column>
				<el-table-column label="文件" min-width="100">
					<template slot-scope="scope">
						<template v-for="item in scope.row.fileList">
							<div class="fileLink">
								<el-link @click="handleDownload(item.url,item.name)" icon="el-icon-document" :underline="false">{{item.name}}</el-link>
							</div>
						</template>
					</template>
				</el-table-column>
				<el-table-column label="收集时间" width="160">
					<template slot-scope="scope">
						<el-date-picker
						  format="yyyy-MM-dd"
						  style="width: 100%;"
						  v-model="scope.row.collectTime"
						  type="date"
						  placeholder="选择日期">
						</el-date-picker>
					</template>
				</el-table-column>
				<el-table-column prop="uploadTime" label="首次上传时间" width="180" :formatter="formatUploadTime">
				</el-table-column>
				<el-table-column prop="updateTime" label="最后上传时间" width="180" :formatter="formatUpdateTime">
				</el-table-column>
				<el-table-column label="操作" width="150">
					<template slot-scope="scope">
						<el-button type="primary" size="small" @click="handleUpload(scope.row)">
							上传
						</el-button>
						<el-button v-if="!scope.row.uploadTime" type="danger" size="small" @click="handleDel(scope.$index, scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
					
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5, 10, 20]" :total="total" style="float:right;">
				</el-pagination>
			</el-col>
			
			<el-drawer
			  title="单证清单"
			  :visible.sync="dialogVisible"
			  direction="ltr"
			  :before-close="handleClose">
				<el-table :data="certifyList" ref="multipleTable" border height="80%" @selection-change="handleSelectionChange">
					<el-table-column type="selection" width="55"></el-table-column>
					<el-table-column property="label" label="单证类型"></el-table-column>
				</el-table>
				<div class="demo-drawer__footer" style="margin-top: 20px;">
					<el-button type="primary" @click="handleAdd">确定</el-button>
					<el-button @click="handleClose">取消</el-button>
				</div>
			</el-drawer>
			
			<el-dialog title="文件上传" :visible.sync="dialogUpload" :before-close="handleClose2">
				<el-upload
				  class="upload-demo"
				  ref="upload"
				  action=""
				  accept=".jpg,.png,.pdf,.jpeg"
				  :file-list="fileList"
				  :auto-upload="false"
				  :on-change="handleChange">
				  <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
				  <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
				  <div slot="tip" class="el-upload__tip">只能上传jpg/png/pdf/jpeg文件，且不超过10M</div>
				</el-upload>
			</el-dialog>
		</el-card>
	</section>
</template>

<script>
	import { getCertifyList,getCollectionList,collectionUpload,fileDownload } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		props: ['reportNo','caseFlag'],
		data() {
			return {
				total: 0,
				page: 1,
				pageSize: 5,
				loading: false,
				dialogVisible: false,
				dialogUpload: false,
				tableData: [],
				certifyList: [],
				casedis: false,
				multipleSelection: [],
				fileList: [],
				uploadParams: {}
			}
		},
		methods: {
			formatCollectTime(row, column) {
				if(row.collectTime) {
					return formatTimeToStr(row.collectTime, 'yyyy-MM-dd');
				} else {
					return '';
				}
			},
			formatUploadTime(row, column) {
				if(row.uploadTime) {
					return formatTimeToStr(row.uploadTime, 'yyyy-MM-dd hh:mm:ss');
				} else {
					return '';
				}
			},
			formatUpdateTime(row, column) {
				if(row.updateTime) {
					return formatTimeToStr(row.updateTime, 'yyyy-MM-dd hh:mm:ss');
				} else {
					return '';
				}
			},
			handleSizeChange(val) {
				this.pageSize = val;
				this.getCollection();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getCollection();
			},
			getCollection() {
				let params = {
					page: this.page,
					pageSize: this.pageSize,
					reportNo: this.reportNo
				};
				this.loading = true;
				getCollectionList(params).then((res) => {
					if(res) {
						this.tableData = res.data.records;
						this.total = res.data.total;
						setTimeout(() => {
							this.loading = false;
						},500);
					}
				});
			},
			handleDel(index,row) {
				this.tableData.splice(index,1);
			},
			handleSelectionChange(val) {
				this.multipleSelection = val;
			},
			handleAdd() {
				this.multipleSelection.forEach((item) => {					
					const isExist = this.tableData.some((item2) => {
						return item2.certifyCode === item.value
					})
					// 如果不存在添加
					if (!isExist) {
						this.tableData.push({
							reportNo: this.reportNo,
							certifyName: item.label,
							certifyCode: item.value
						})
					}
				})
				this.$refs.multipleTable.clearSelection();
				this.dialogVisible = false;
			},
			handleClose() {
				this.$refs.multipleTable.clearSelection();
				this.dialogVisible = false;
			},
			handleClose2() {
				this.$refs.upload.clearFiles();
				this.dialogUpload = false;
			},
			handleUpload(row) {
				if(!row.collectTime) {
					this.$message.warning('收集时间不能为空');
					return false;
				}
				this.uploadParams = row;
				this.dialogUpload = true;
			},
			handleChange(file,fileList) {
				if(file.size/(1024*1024) > 10) {
					this.$message.warning('当前限制文件大小不能大于10M');
					fileList.pop();
				}
				let allowSuffix = ['jpg','png','jpeg','pdf'];
				let suffix = file.name.slice(file.name.lastIndexOf('.')+1).toLowerCase();
				if(allowSuffix.indexOf(suffix) == -1) {
					this.$message.warning('文件格式不符合要求');
					fileList.pop();
				}
			},
			handleDownload(url,name) {
				let params = {
					url: url,
					name: name
				}
				fileDownload(params).then((res) => {
					let blob = res.data;
					let linkElement = document.createElement('a');
					let fileUrl = window.URL.createObjectURL(blob);
					linkElement.setAttribute('href', fileUrl);
					linkElement.setAttribute("download", name);
					let clickEvent = new MouseEvent("click", {
						"view": window,
						"bubbles": true,
						"cancelable": false
					});
					linkElement.dispatchEvent(clickEvent);
				});
			},
			submitUpload() {
				let fd = new FormData();
				let uploadFiles = this.$refs.upload.uploadFiles;
				uploadFiles.forEach((item) => {
					fd.append('files',item.raw,item.name)
				})
				fd.append('reportNo',this.uploadParams.reportNo);
				fd.append('certifyName',this.uploadParams.certifyName);
				fd.append('certifyCode',this.uploadParams.certifyCode);
				fd.append('collectTime',formatTimeToStr(this.uploadParams.collectTime, 'yyyy-MM-dd hh:mm:ss'));
				collectionUpload(fd).then((res) => {
					let collection = res.data.data;
					let index = '';
					this.tableData.forEach((item,i) => {
						if(item.certifyCode == collection.certifyCode) {
							index = i;
							return false;
						}
					});
					this.tableData.splice(index,1,collection);
					this.$message.success('上传完毕');
					this.handleClose2();
				})
			},
			cellClass () {
				return 'text-align: center;'
			},
			headCellClass () {
				return 'background-color: #f5f7fa !important;text-align: center;'
			},
			selectCollection() {
				this.dialogVisible = true;
			},
			getCertify() {
				getCertifyList().then((res) => {
					this.certifyList = res.data.data;
				})
			}
		},
		mounted() {
			this.getCertify();
			this.getCollection();
			if(this.caseFlag == '04') {
				this.casedis = true;
			}
		}
	}
</script>

<style>
	.fileLink:hover {
		background-color: #bec2ca;
	}
</style>
