<template>
	<section>
		<el-card shadow="hover" style="margin-bottom: 20px;">
			<!--工具条-->
			<el-col :span="24" class="toolbar" style="background: #fff;padding-bottom: 0px;">
				<el-form :inline="true" :model="filters">
					<el-form-item label="机构名称">
						<el-input v-model="filters.name" placeholder="支持模糊查询"></el-input>
					</el-form-item>
					<el-form-item label="机构级别">
						<el-select v-model="filters.comLevel">
							<el-option value=""></el-option>
							<el-option value="1" label="1">
								<span style="float: left">1</span>
								<span style="float: right; color: #8492a6; font-size: 13px">分公司</span>
							</el-option>
							<el-option value="2" label="2">
								<span style="float: left">2</span>
								<span style="float: right; color: #8492a6; font-size: 13px">部门</span>
							</el-option>
							<el-option value="3" label="3">
								<span style="float: left">3</span>
								<span style="float: right; color: #8492a6; font-size: 13px">小组</span>
							</el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="有效标志">
						<el-switch v-model="filters.validStatus" active-value="1" inactive-value="0"></el-switch>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" v-on:click="getCompany">查询</el-button>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" @click="handleAdd">新增</el-button>
					</el-form-item>
				</el-form>
			</el-col>
		
			<!--列表-->
			<el-table :data="tableData" highlight-current-row v-loading="listLoading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column prop="comCode" label="机构代码" min-width="120" sortable>
				</el-table-column>
				<el-table-column prop="comName" label="机构名称" min-width="120">
				</el-table-column>
				<el-table-column prop="upperComCode" label="上级机构" min-width="120">
				</el-table-column>
				<el-table-column prop="claimComCode" label="归属机构代码" min-width="120">
				</el-table-column>
				<el-table-column prop="claimComName" label="归属机构名称" min-width="120">
				</el-table-column>
				<el-table-column prop="comLevel" label="机构级别" width="120" sortable>
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
		
			<!--工具条-->
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5, 10, 20]" :total="total" style="float:right;">
				</el-pagination>
			</el-col>
		</el-card>
		
		<!--编辑界面-->
		<el-dialog title="编辑" :visible.sync="editFormVisible" :close-on-click-modal="false">
			<el-form :model="editForm" label-width="80px" :rules="editFormRules" ref="editForm">
				<el-form-item label="机构代码" prop="comCode">
					<el-input v-model="editForm.comCode" :disabled="true"></el-input>
				</el-form-item>
				<el-form-item label="机构名称" prop="comName">
					<el-input v-model="editForm.comName"></el-input>
				</el-form-item>
				<el-form-item label="上级机构" prop="upperComCode">
					<el-select v-model="editForm.upperComCode">
						<el-option v-for="item in upperComList"
						:key="item.value"
						:label="item.label"
						:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="归属机构" prop="claimComCode">
					<el-select v-model="editForm.claimComCode" @change="selectClaimCom">
						<el-option v-for="item in claimComList"
						:key="item.value"
						:label="item.label"
						:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<input type="hidden" v-model="editForm.claimComName" />
				<el-form-item label="机构级别" prop="comLevel">
					<el-select v-model="editForm.comLevel">
						<el-option value="1" label="1">
							<span style="float: left">1</span>
							<span style="float: right; color: #8492a6; font-size: 13px">分公司</span>
						</el-option>
						<el-option value="2" label="2">
							<span style="float: left">2</span>
							<span style="float: right; color: #8492a6; font-size: 13px">部门</span>
						</el-option>
						<el-option value="3" label="3">
							<span style="float: left">3</span>
							<span style="float: right; color: #8492a6; font-size: 13px">小组</span>
						</el-option>
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
			<el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm">
				<el-form-item label="机构代码" prop="comCode">
					<el-input v-model="addForm.comCode"></el-input>
				</el-form-item>
				<el-form-item label="机构名称" prop="comName">
					<el-input v-model="addForm.comName"></el-input>
				</el-form-item>
				<el-form-item label="上级机构" prop="upperComCode">
					<el-select v-model="addForm.upperComCode">
						<el-option v-for="item in upperComList"
						:key="item.value"
						:label="item.label"
						:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="归属机构" prop="claimComCode">
					<el-select v-model="addForm.claimComCode" @change="selectClaimCom2">
						<el-option v-for="item in claimComList"
						:key="item.value"
						:label="item.label"
						:value="item.value"></el-option>
					</el-select>
				</el-form-item>
				<input type="hidden" v-model="addForm.claimComName" />
				<el-form-item label="机构级别" prop="comLevel">
					<el-select v-model="addForm.comLevel">
						<el-option value="1" label="1">
							<span style="float: left">1</span>
							<span style="float: right; color: #8492a6; font-size: 13px">分公司</span>
						</el-option>
						<el-option value="2" label="2">
							<span style="float: left">2</span>
							<span style="float: right; color: #8492a6; font-size: 13px">部门</span>
						</el-option>
						<el-option value="3" label="3">
							<span style="float: left">3</span>
							<span style="float: right; color: #8492a6; font-size: 13px">小组</span>
						</el-option>
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
	import { getCompanyList,getCompanyLevel,editCompany,addCompany,checkComCode } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		data() {
			var validatePass = (rule, value, callback) => {
				if (value === '') {
					callback(new Error('请输入机构代码'));
				} else {
					checkComCode(params).then((res) => {
						if(res) {
							callback(new Error('机构代码已存在'));
						} else {
							callback();
						}
					});
				}
			};
			return {
				filters: {
					name: '',
					comLevel: '',
					validStatus: '1'
				},
				total: 0,
				page: 1,
				pageSize: 5,
				editFormVisible: false,//编辑界面是否显示
				editLoading: false,
				editFormRules: {
					comName: [
						{ required: true, message: '请输入机构名称', trigger: 'blur' }
					]
				},
				//编辑界面数据
				editForm: {
					comCode: '',
					comName: '',
					upperComCode: '',
					claimComCode: '',
					claimComName: '',
					comLevel: '',
					validStatus: ''
				},
				addFormVisible: false,//新增界面是否显示
				addLoading: false,
				addFormRules: {
					comName: [
						{ required: true, message: '请输入机构名称', trigger: 'blur' }
					],
					comCode: [
						{ required: true, trigger: 'blur', 
							validator: (rule, value, callback) => {
								if (value === '') {
									callback(new Error('请输入机构代码'));
								} else {
									let params = {
										comCode: value
									}
									checkComCode(params).then((res) => {
										if(res.data) {
											callback(new Error('机构代码已存在'));
										} else {
											callback();
										}
									});
								}
							} 
						}
					],
				},
				//新增界面数据
				addForm: {
					comCode: '',
					comName: '',
					upperComCode: '',
					claimComCode: '',
					claimComName: '',
					comLevel: '',
					validStatus: ''
				},
				claimComList: [{"label":"","value":""}],
				upperComList: [],
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
			handleSizeChange(val) {
				this.pageSize = val;
				this.getCompany();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getCompany();
			},
			//获取机构列表
			getCompany() {
				let params = {
					page: this.page,
					pageSize: this.pageSize,
					comName: this.filters.name,
					comLevel: this.filters.comLevel,
					validStatus: this.filters.validStatus
				};
				this.listLoading = true;
				//NProgress.start();
				getCompanyList(params).then((res) => {
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
				this.getUpperComList(row.claimComCode);
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
							editCompany(params).then((res) => {
								this.editLoading = false;
								//NProgress.done();
								this.$message({
									message: '提交成功',
									type: 'success'
								});
								this.$refs['editForm'].resetFields();
								this.editFormVisible = false;
								this.getCompany();
							});
						});
					}
				});
			},
			//显示新增界面
			handleAdd: function (index, row) {
				this.addFormVisible = true;
				this.addForm = {
					comCode: '',
					comName: '',
					upperComCode: '',
					claimComCode: '',
					claimComName: '',
					comLevel: '1',
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
							addCompany(params).then((res) => {
								this.addLoading = false;
								//NProgress.done();
								this.$message({
									message: '提交成功',
									type: 'success'
								});
								this.$refs['addForm'].resetFields();
								this.addFormVisible = false;
								this.getCompany();
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
			getClaimComList () {
				let params = {
					comLevel: '1'
				};
				getCompanyLevel(params).then((res) => {
					if(res) {
						this.claimComList = this.claimComList.concat(res.data.data);
					}
				});
			},
			getUpperComList (claimComCode,flag) {
				if(!claimComCode) {
					this.editForm.upperComCode = "";
					this.upperComList = [];
					return;
				}
				let params = {
					comLevel: '-1',
					comCode: claimComCode
				};
				getCompanyLevel(params).then((res) => {
					if(res) {
						this.upperComList = res.data.data;
						if(flag == '1') {
							this.editForm.upperComCode = this.upperComList[0].value;
						}
						if(flag == '2') {
							this.addForm.upperComCode = this.upperComList[0].value;
						}
					}
				});
			},
			selectClaimCom(code) {
				let obj = {};
				 obj = this.claimComList.find((item)=>{ //这里的claimComList就是上面遍历的数据源
				   return item.value === code;//筛选出匹配数据
				 });
				 this.editForm.claimComName = obj.label;
				 this.getUpperComList(obj.value,'1');
			},
			selectClaimCom2(code) {
				let obj = {};
				 obj = this.claimComList.find((item)=>{ //这里的claimComList就是上面遍历的数据源
				   return item.value === code;//筛选出匹配数据
				 });
				 this.addForm.claimComName = obj.label;
				 this.getUpperComList(obj.value,'2');
			}
		},
		mounted() {
			this.getCompany();
			this.getClaimComList();
		}
	}
</script>

<style>
</style>
