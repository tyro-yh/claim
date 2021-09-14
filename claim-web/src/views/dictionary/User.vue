<template>
	<section>
		<el-card shadow="hover" style="margin-bottom: 20px;">
			<!--工具条-->
			<el-col :span="24" class="toolbar" style="background: #fff;padding-bottom: 0px;">
				<el-form :inline="true" :model="filters">
					<el-form-item label="员工名称">
						<el-input v-model="filters.name" placeholder="支持模糊查询"></el-input>
					</el-form-item>
					<el-form-item label="员工号">
						<el-input v-model="filters.code" placeholder="请输入完成工号"></el-input>
					</el-form-item>
					<el-form-item label="有效标志">
						<el-switch v-model="filters.validStatus" active-value="1" inactive-value="0"></el-switch>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" v-on:click="getUser">查询</el-button>
					</el-form-item>
					<!-- <el-form-item>
						<el-button type="primary" @click="handleAdd">新增</el-button>
					</el-form-item> -->
				</el-form>
			</el-col>
		
			<!--列表-->
			<el-table :data="tableData" highlight-current-row v-loading="listLoading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column prop="userCode" label="员工号" min-width="120" sortable>
				</el-table-column>
				<el-table-column prop="userName" label="员工名称" min-width="120">
				</el-table-column>
				<el-table-column prop="comCode" label="业务归属机构" min-width="120">
				</el-table-column>
				<el-table-column prop="makeCom" label="出单归属机构" min-width="120">
				</el-table-column>
				<el-table-column prop="validStatus" label="有效标志" width="80" :formatter="formatValid">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="100" :formatter="formatCreateTime">
				</el-table-column>
				<el-table-column prop="updateTime" label="更新时间" width="100" :formatter="formatUpdateTime">
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" type="danger" v-show="scope.row.validStatus=='1'" @click="changeValid(scope.$index, scope.row)">无效</el-button>
						<el-button size="small" type="primary" v-show="scope.row.validStatus=='0'" @click="changeValid(scope.$index, scope.row)">有效</el-button>
					</template>
				</el-table-column>
			</el-table>
			
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5, 10, 20]" :total="total" style="float:right;">
				</el-pagination>
			</el-col>
		</el-card>
	</section>
</template>

<script>
	import { getUserList,editUser } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		data() {
			return {
				filters: {
					name: '',
					code: '',
					validStatus: '1'
				},
				total: 0,
				page: 1,
				pageSize: 5,
				listLoading: false,
				tableData: []
			}
		},
		methods: {
			//性别显示转换
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
				this.getUser();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getUser();
			},
			//获取用户列表
			getUser() {
				let params = {
					page: this.page,
					pageSize: this.pageSize,
					userName: this.filters.name,
					userCode: this.filters.code,
					validStatus: this.filters.validStatus
				};
				this.listLoading = true;
				//NProgress.start();
				getUserList(params).then((res) => {
					if(res) {
						this.tableData = res.data.records;
						this.total = res.data.total;
						this.listLoading = false;
					}
					//NProgress.done();
				});
			},
			//显示编辑界面
			changeValid: function (index, row) {
				this.$confirm('确认提交吗？', '提示', {}).then(() => {
					//NProgress.start();
					let params = Object.assign({}, row);
					editUser(params).then((res) => {
						//NProgress.done();
						this.$message({
							message: '提交成功',
							type: 'success'
						});
						this.getUser();
					});
				});
			},
			cellClass () {
				return 'text-align: center;'
			},
			headCellClass () {
				return 'background-color: #f5f7fa !important;text-align: center;'
			}
		},
		mounted() {
			this.getUser();
		}
	}
</script>

<style>
</style>
