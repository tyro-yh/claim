<template>
	<section>
		<el-card shadow="hover" style="margin-bottom: 20px;">
			<!--工具条-->
			<el-col :span="24" class="toolbar" style="background: #fff;padding-bottom: 0px;">
				<el-form :inline="true" :model="filters">
					<el-form-item label="字典名称">
						<el-input v-model="filters.name" placeholder="支持模糊查询"></el-input>
					</el-form-item>
					<el-form-item label="字典类型">
						<el-select v-model="filters.dataType">
							<el-option v-for="item in commonTypeList"
							:key="item.value"
							:label="item.label"
							:value="item.value"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="有效标志">
						<el-switch v-model="filters.validStatus" active-value="1" inactive-value="0"></el-switch>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="getCommon">查询</el-button>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="handleAdd">新增</el-button>
					</el-form-item>
				</el-form>
			</el-col>
		
			<!--列表-->
			<el-table :data="tableData" highlight-current-row v-loading="listLoading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column prop="dataTypeCode" label="字典类型代码" min-width="120" sortable>
				</el-table-column>
				<el-table-column prop="dataTypeName" label="字典类型名称" min-width="120">
				</el-table-column>
				<el-table-column prop="dataCode" label="字典代码" min-width="100">
				</el-table-column>
				<el-table-column prop="dataName" label="字典名称" min-width="120">
				</el-table-column>
				<el-table-column prop="preType" label="上级类型" min-width="120" :formatter="formatPreType">
				</el-table-column>
				<el-table-column prop="preCode" label="上级代码" min-width="100">
				</el-table-column>
				<el-table-column prop="validStatus" label="有效标志" width="80" :formatter="formatValid">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="100" :formatter="formatCreateTime">
				</el-table-column>
				<el-table-column prop="updateTime" label="更新时间" width="100" :formatter="formatUpdateTime">
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
					</template>
				</el-table-column>
			</el-table>
		
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5, 10, 20]" :total="total" style="float:right;">
				</el-pagination>
			</el-col>
		</el-card>
	
		<!--编辑界面-->
		<el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="100px" :rules="editFormRules" ref="editForm">
				<el-form-item label="字典类型代码" prop="dataTypeCode">
					<el-input v-model="editForm.dataTypeCode"></el-input>
				</el-form-item>
				<el-form-item label="字典类型名称" prop="dataTypeName">
					<el-input v-model="editForm.dataTypeName"></el-input>
				</el-form-item>
				<el-form-item label="字典代码" prop="dataCode">
					<el-input v-model="editForm.dataCode"></el-input>
				</el-form-item>
				<el-form-item label="字典名称" prop="dataName">
					<el-input v-model="editForm.dataName"></el-input>
				</el-form-item>
				<el-form-item label="上级类型" prop="preType">
					<el-select v-model="editForm.preType" @change="selectPreType(editForm.preType,'1')">
						<el-option v-for="item in commonTypeList"
						:key="item.value"
						:label="item.label"
						:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="上级代码" prop="preCode">
					<el-select v-model="editForm.preCode" >
						<el-option v-for="item in preCodeList"
						:key="item.value"
						:label="item.label"
						:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="有效标志" prop="validStatus">
					<el-switch v-model="editForm.validStatus" active-value="1" inactive-value="0"></el-switch>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="editFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
			</div>
		</el-dialog>
	
		<!--新增界面-->
		<el-dialog title="新增" :visible.sync="addFormVisible" :close-on-click-modal="false">
			<el-form :model="addForm" label-width="100px" :rules="addFormRules" ref="addForm">
				<el-form-item label="字典类型代码" prop="dataTypeCode">
					<el-input v-model="addForm.dataTypeCode"></el-input>
				</el-form-item>
				<el-form-item label="字典类型名称" prop="dataTypeName">
					<el-input v-model="addForm.dataTypeName"></el-input>
				</el-form-item>
				<el-form-item label="字典代码" prop="dataCode">
					<el-input v-model="addForm.dataCode"></el-input>
				</el-form-item>
				<el-form-item label="字典名称" prop="dataName">
					<el-input v-model="addForm.dataName"></el-input>
				</el-form-item>
				<el-form-item label="上级类型" prop="preType">
					<el-select v-model="addForm.preType" @change="selectPreType(addForm.preType,'2')">
						<el-option v-for="item in commonTypeList"
						:key="item.value"
						:label="item.label"
						:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="上级代码" prop="preCode">
					<el-select v-model="addForm.preCode" >
						<el-option v-for="item in preCodeList"
						:key="item.value"
						:label="item.label"
						:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="有效标志" prop="validStatus">
					<el-switch v-model="addForm.validStatus" active-value="1" inactive-value="0"></el-switch>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click.native="addFormVisible = false">取消</el-button>
				<el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
			</div>
		</el-dialog>
	</section>
</template>

<script>
	import { getCommonList,getCommonDataType,editCommon,addCommon,selectByDataType } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		data() {
			return {
				filters: {
					name: '',
					dataType: '',
					validStatus: '1'
				},
				total: 0,
				page: 1,
				pageSize: 5,
				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					dataCode: [
						{ required: true, message: '请输入字典代码', trigger: 'blur' }
					]
				},
				//编辑界面数据
				editForm: {
					dataTypeCode: '',
					dataTypeName: '',
					dataCode: '',
					dataName: '',
					preType: '',
					preCode: '',
					validStatus: ''
				},
				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
					dataCode: [
						{ required: true, message: '请输入字典代码', trigger: 'blur' }
					]
				},
				//新增界面数据
				addForm: {
					dataTypeCode: '',
					dataTypeName: '',
					dataCode: '',
					dataName: '',
					preType: '',
					preCode: '',
					validStatus: ''
				},
				commonTypeList: [{"label":"","value":""}],
				preCodeList: [],
				listLoading: false,
				tableData: []
			}
		},
		methods: {
			//有效标志显示转换
			formatValid: function (row, column) {
				return row.validStatus == 1 ? '有效' : row.validStatus == 0 ? '无效' : '未知';
			},
			formatCreateTime: function (row, column) {
				return formatTimeToStr(row.createTime, 'yyyy-MM-dd');
			},
			formatUpdateTime: function (row, column) {
				return formatTimeToStr(row.updateTime, 'yyyy-MM-dd');
			},
			formatPreType(row, column) {
				if(row.preType) {
					let obj = [];
					obj = this.commonTypeList.filter(item => {
						return item.value == row.preType
					});
					if(obj.length > 0) {
						return obj[0].label;
					}
				}
			},
			handleSizeChange(val) {
				this.pageSize = val;
				this.getCommon();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getCommon();
			},
			//获取字典列表
			getCommon() {
				let params = {
					page: this.page,
					pageSize: this.pageSize,
					dataName: this.filters.name,
					dataTypeCode: this.filters.dataType,
					validStatus: this.filters.validStatus
				};
				this.listLoading = true;
				//NProgress.start();
				getCommonList(params).then((res) => {
					if(res) {
						this.tableData = res.data.records;
						this.total = res.data.total;
						this.listLoading = false;
					}
					//NProgress.done();
				});
			},
			//显示编辑界面
			handleEdit: function (index, row) {
				// this.getUpperComList(row.claimComCode);
				this.editFormVisible = true;
				this.editForm = Object.assign({}, row);
			},
			//编辑
			editSubmit: function () {
				this.$refs.editForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() => {
							this.editLoading = true;
							//NProgress.start();
							let params = Object.assign({}, this.editForm);
							editCommon(params).then((res) => {
								this.editLoading = false;
								//NProgress.done();
								this.$message({
									message: '提交成功',
									type: 'success'
								});
								this.$refs['editForm'].resetFields();
								this.editFormVisible = false;
								this.getCommon();
							});
						});
					}
				});
			},
			//显示新增界面
			handleAdd: function (index, row) {
				this.addFormVisible = true;
				this.addForm = {
					dataTypeCode: '',
					dataTypeName: '',
					dataCode: '',
					dataName: '',
					preType: '',
					preCode: '',
					validStatus: '1'
				};
			},
			//编辑
			addSubmit: function () {
				this.$refs.addForm.validate((valid) => {
					if (valid) {
						this.$confirm('确认提交吗？', '提示', {}).then(() => {
							this.addLoading = true;
							//NProgress.start();
							let params = Object.assign({}, this.addForm);
							addCommon(params).then((res) => {
								this.addLoading = false;
								//NProgress.done();
								this.$message({
									message: '提交成功',
									type: 'success'
								});
								this.$refs['addForm'].resetFields();
								this.addFormVisible = false;
								this.getCommon();
							});
						});
					}
				});
			},
			cellClass () {
				return 'text-align: center;'
			},
			headCellClass () {
				return 'background-color: #f5f7fa !important;text-align: center;'
			},
			getTypeList () {
				getCommonDataType().then((res) => {
					if(res) {
						this.commonTypeList = this.commonTypeList.concat(res.data.data);
					}
				});
			},
			selectPreType (code,flag) {
				if(flag == '1' && !code) {
					this.editForm.preCode = "";
					this.preCodeList = [];
					return;
				}
				if(flag == '2' && !code) {
					this.addForm.preCode = "";
					this.preCodeList = [];
					return;
				}
				
				let params = {
					dataTypeCode: code
				};
				selectByDataType(params).then((res) => {
					if(res) {
						this.preCodeList = res.data.data;
						if(flag == '1') {
							this.editForm.preCode = this.preCodeList[0].value;
						}
						if(flag == '2') {
							this.addForm.preCode = this.preCodeList[0].value;
						}
					}
				});
			}
		},
		mounted() {
			this.getCommon();
			this.getTypeList();
		}
	}
</script>

<style>
</style>
