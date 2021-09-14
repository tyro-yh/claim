<template>
	<section>
		<el-row :gutter="24">
			<el-col :span="8">
				<el-card shadow="hover" class="mgb20" style="height:252px;">
					<el-row>
					<div class="user-info">
						<img src="@/assets/img/img.jpg" class="user-avator" alt />
						<div class="user-info-cont">
							<div class="user-info-name">{{ sysUserName }}</div>
							<div>{{ sysComFullName }}</div>
						</div>
					</div>
					<div class="user-info-list">
						上次登录时间：
						<span>
							{{ lastLoginTime }}
							<el-button v-popover:popover type="text">更多</el-button>
						</span>
						<el-popover
							ref="popover"
							placement="top-start"
							title="登录历史"
							width="200"
							trigger="hover">
							<el-timeline>
								<el-timeline-item
									v-for="(activity, index) in activities"
									:key="index"
									icon="el-icon-alarm-clock"
									type="primary"
									size="large"
									:timestamp="activity.LoginTime">
							    </el-timeline-item>
							</el-timeline>
						</el-popover>
					</div>
					</el-row>
				</el-card>
				<el-card shadow="hover" style="height:252px;">
					<template #header>
						<div class="clearfix">
							<span>全司出险比</span>
						</div>
					</template>
					<template v-for="(item,index) in scaleData">
						{{item.Insurance}}
						<el-progress :percentage="item.cnum*100" v-if="index==0" color="#42b983"></el-progress>
						<el-progress :percentage="item.cnum*100" v-if="index==1" color="#f1e05a"></el-progress>
						<el-progress :percentage="item.cnum*100" v-if="index==2"></el-progress>
						<el-progress :percentage="item.cnum*100" v-if="index==3" color="#f56c6c"></el-progress>
					</template>
				</el-card>
			</el-col>
			<el-col :span="16">
			    <el-row :gutter="20" class="mgb20">
			        <el-col :span="8">
			            <el-card shadow="hover" :body-style="{ padding: '0px' }">
			                <div class="grid-content grid-con-1">
			                    <i class="el-icon-alarm-clock grid-con-icon"></i>
			                    <div class="grid-cont-right">
			                        <div class="grid-num">{{done}}</div>
			                        <div>已处理</div>
			                    </div>
			                </div>
			            </el-card>
			        </el-col>
			        <el-col :span="8">
			            <el-card shadow="hover" :body-style="{ padding: '0px' }">
			                <div class="grid-content grid-con-2">
			                    <i class="el-icon-message-solid grid-con-icon"></i>
			                    <div class="grid-cont-right">
			                        <div class="grid-num">{{doing}}</div>
			                        <div>处理中</div>
			                    </div>
			                </div>
			            </el-card>
			        </el-col>
			        <el-col :span="8">
			            <el-card shadow="hover" :body-style="{ padding: '0px' }">
			                <div class="grid-content grid-con-3">
			                    <i class="el-icon-s-goods grid-con-icon"></i>
			                    <div class="grid-cont-right">
			                        <div class="grid-num">{{todo}}</div>
			                        <div>待处理</div>
			                    </div>
			                </div>
			            </el-card>
			        </el-col>
			    </el-row>
			    <el-card shadow="hover" style="height:403px;">
			        <template #header>
			            <div class="clearfix">
			                <span>待办事项</span>
			                <el-button style="float: right; padding: 3px 0" type="text" @click="handleAddTodo">添加</el-button>
			            </div>
			        </template>
			
			        <el-table v-loading="todoListLoading" :show-header="false" :data="todoList" style="width:100%;">
			            <el-table-column width="40">
			                <template #default="scope">
			                    <el-checkbox v-model="scope.row.status" @change="handleEditTodo(scope.$index, scope.row)"></el-checkbox>
			                </template>
			            </el-table-column>
			            <el-table-column>
			                <template #default="scope">
			                    <div class="todo-item" :class="{
			                            'todo-item-del': scope.row.status,
			                        }">{{ scope.row.title }}</div>
			                </template>
			            </el-table-column>
			            <el-table-column width="60">
			                <template #default="scope">
								<el-button circle icon="el-icon-delete" @click="handleDelTodo(scope.$index, scope.row)"></el-button>
			                </template>
			            </el-table-column>
			        </el-table>
			    </el-card>
			</el-col>
		</el-row>
		
		<el-card shadow="hover" style="margin-bottom: 20px;">
			<!--列表-->
			<el-table :data="tableData.filter(data => !filters.search || data.businessKey.toLowerCase().includes(filters.search.toLowerCase()))" highlight-current-row v-loading="listLoading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column type="index" width="60">
				</el-table-column>
				<el-table-column prop="reportNo" label="报案号" min-width="120" sortable>
				</el-table-column>
				<el-table-column prop="policyNo" label="保单号" min-width="120">
				</el-table-column>
				<el-table-column prop="businessKey" label="业务号" min-width="120">
				</el-table-column>
				<el-table-column prop="taskType" label="任务类型" width="100">
				</el-table-column>
				<el-table-column prop="remark" label="任务备注" min-width="120">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="100" :formatter="formatCreateTime">
				</el-table-column>
				<el-table-column
				  min-width="120">
				  <template slot="header" slot-scope="scope">
					<el-input
					  v-model="filters.search"
					  size="mini"
					  placeholder="输入业务号搜索">
					  <el-button slot="append" icon="el-icon-refresh" @click="getWorkflow"></el-button>
					</el-input>
				  </template>
				  <template slot-scope="scope">
					<el-button
					  type="primary" size="mini"
					  @click="showWorkflow(scope.$index, scope.row)"
					  >流程图</el-button>
					<el-button
					  size="mini"
					  @click="handleEdit(scope.$index, scope.row)">查看</el-button>
				  </template>
				</el-table-column>
			</el-table>
			
			<!--工具条-->
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5, 10, 20]" :total="total" style="float:right;">
				</el-pagination>
			</el-col>
		</el-card>
	</section>
</template>

<script>
	import { getMyTask,initTaskStatus,insuranceScale,selectTodoList,addTodo,delTodo,editTodo,selectLoginHis } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		data() {
			return {
				filters: {
					search: ''
				},
				todoListLoading: false,
				sysUserName: '',
				sysUserCode: '',
				sysComFullName: '',
				lastLoginTime: '',
				total: 0,
				page: 1,
				pageSize: 5,
				listLoading: false,
				tableData: [],
				scaleData: [],
				todoList: [],
				activities: [],
				todo: '',
				doing: '',
				done: ''
			}
		},
		methods: {
			formatCreateTime: function (row, column) {
				return formatTimeToStr(row.createTime, 'yyyy-MM-dd');
			},
			handleSizeChange(val) {
				this.pageSize = val;
				this.getWorkflow();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getWorkflow();
			},
			//获取机构列表
			getWorkflow() {
				let params = Object.assign(
				{
					page: this.page,
					pageSize: this.pageSize,
				}, JSON.parse(sessionStorage.getItem('user')));
				this.listLoading = true;
				//NProgress.start();
				getMyTask(params).then((res) => {
					this.tableData = res.data.records;
					this.total = res.data.total;
					this.listLoading = false;
					//NProgress.done();
				});
			},
			cellClass () {
				return 'text-align: center;'
			},
			headCellClass () {
				return 'background-color: #f5f7fa !important;text-align: center;'
			},
			handleDelete(index, row) {
				console.log(index, row);
			},
			handleAddTodo () {
				if(this.todoList.length >= 5) {
					this.$message.error('待办过多，请删除已完成待办');
					return;
				}
				this.$prompt('请输入待办', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					inputValidator: (value) => {
						if(!value) {
							return '请输入！'
						}
					}
				}).then(({ value }) => {
					let params = {
						userCode: this.sysUserCode,
						title: value,
						status: false
					}
					this.todoListLoading = true;
					addTodo(params).then(() => {
						selectTodoList(params).then((res) => {
							this.todoList = res.data.data;
							setTimeout(() =>{
								this.todoListLoading = false;
							},500);
						});
					});
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '取消输入'
					});       
				});
			},
			handleEditTodo(index, row) {
				let params = Object.assign({},row);
				editTodo(params);
			},
			handleDelTodo(index, row) {
				this.$confirm('是否删除该待办, 是否继续?', '提示', {
				  confirmButtonText: '确定',
				  cancelButtonText: '取消',
				  type: 'warning'
				}).then(() => {
				  let params = {
					id: row.id
				  }
				  let params2 = {
					userCode: this.sysUserCode
				  }
				  this.todoListLoading = true;
				  delTodo(params).then(() => {
					selectTodoList(params2).then((res) => {
						this.todoList = res.data.data;
						setTimeout(() =>{
							this.todoListLoading = false;
						},500);
					});
				  });
				}).catch(() => {
				  this.$message({
					type: 'info',
					message: '已取消删除'
				  });          
				});
			},
			showWorkflow(index, row) {
				let routeUrl = this.$router.resolve({
					name: "Workflow",
					params: {reportNo:row.reportNo}
				});
				window.open(routeUrl.href, '_blank');
				// this.$router.push({name:'Workflow',params:{reportNo:row.reportNo}})
			}
		},
		mounted() {
			let user = sessionStorage.getItem('user');
			if (user) {
				user = JSON.parse(user);
				this.sysUserName = user.userName || '';
				this.sysUserCode = user.userCode || '';
				this.sysComFullName = user.comFullName || '';
				this.lastLoginTime = formatTimeToStr(user.lastLoginTime,'yyyy-MM-dd hh:mm:ss') || '';
			}
			this.getWorkflow();
			let params = {
				userCode: user.userCode
			}
			initTaskStatus(params).then((res) => {
				let taskData = res.data.data[0];
				this.todo = taskData.todo;
				this.doing = taskData.doing;
				this.done = taskData.done;
			});
			insuranceScale().then((res) => {
				this.scaleData = res.data.data;
			});
			selectTodoList(params).then((res) => {
				this.todoList = res.data.data;
			});
			selectLoginHis(params).then((res) => {
				this.activities = res.data.data;
			})
		}
	}
</script>

<style>
	.el-row {
	    margin-bottom: 20px;
	}
	
	.grid-content {
	    display: flex;
	    align-items: center;
	    height: 100px;
	}
	
	.grid-cont-right {
	    flex: 1;
	    text-align: center;
	    font-size: 14px;
	    color: #999;
	}
	
	.grid-num {
	    font-size: 30px;
	    font-weight: bold;
	}
	
	.grid-con-icon {
	    font-size: 50px;
	    width: 100px;
	    height: 100px;
	    text-align: center;
	    line-height: 100px;
	    color: #fff;
	}
	
	.grid-con-1 .grid-con-icon {
	    background: rgb(45, 140, 240);
	}
	
	.grid-con-1 .grid-num {
	    color: rgb(45, 140, 240);
	}
	
	.grid-con-2 .grid-con-icon {
	    background: rgb(100, 213, 114);
	}
	
	.grid-con-2 .grid-num {
	    color: rgb(45, 140, 240);
	}
	
	.grid-con-3 .grid-con-icon {
	    background: rgb(242, 94, 67);
	}
	
	.grid-con-3 .grid-num {
	    color: rgb(242, 94, 67);
	}
	
	.user-info {
	    display: flex;
	    align-items: center;
	    padding-bottom: 20px;
	    border-bottom: 2px solid #ccc;
	    margin-bottom: 20px;
	}
	
	.user-avator {
	    width: 120px;
	    height: 120px;
	    border-radius: 50%;
	}
	
	.user-info-cont {
	    padding-left: 50px;
	    flex: 1;
	    font-size: 14px;
	    color: #999;
	}
	
	.user-info-cont div:first-child {
	    font-size: 30px;
	    color: #222;
	}
	
	.user-info-list {
	    font-size: 14px;
	    color: #999;
	    line-height: 25px;
	}
	
	.user-info-list span {
	    margin-left: 50px;
	}
	
	.mgb20 {
	    margin-bottom: 20px;
	}
	
	.todo-item {
	    font-size: 14px;
	}
	
	.todo-item-del {
	    text-decoration: line-through;
	    color: #999;
	}
</style>
