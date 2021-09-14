<template>
	<section>
		<el-card shadow="hover">
			<!--工具条-->
			<el-col :span="24" class="toolbar" style="background: #fff;padding-bottom: 0px;">
				<el-form :inline="true" :model="filters">
					<el-form-item label="保单号">
						<el-input v-model="filters.policyNo" placeholder="请输入保单号"></el-input>
					</el-form-item>
					<el-form-item label="投保人证件">
						<el-input v-model="filters.appliCode"></el-input>
					</el-form-item>
					<el-form-item label="被保险人证件">
						<el-input v-model="filters.insuredCode"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" v-on:click="getPolicy">查询</el-button>
					</el-form-item>
				</el-form>
			</el-col>
			<!--列表-->
			<el-table :data="tableData" highlight-current-row v-loading="listLoading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column prop="policyNo" label="保单号" min-width="120" sortable>
				</el-table-column>
				<el-table-column prop="insuranceName" label="险类" min-width="120">
				</el-table-column>
				<el-table-column prop="insuredName" label="被保险人名称" min-width="100">
				</el-table-column>
				<el-table-column prop="insuredCode" label="被保险人证件" min-width="120">
				</el-table-column>
				<el-table-column prop="startTime" label="起保日期" min-width="120" :formatter="formatStartTime">
				</el-table-column>
				<el-table-column prop="endTime" label="终保日期" min-width="120" :formatter="formatEndTime">
				</el-table-column>
				<el-table-column prop="policyStatus" label="保单状态" width="80" :formatter="formatStatus">
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" @click="selectPolicy(scope.$index, scope.row)">详情</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5, 10, 20]" :total="total" style="float:right;">
				</el-pagination>
			</el-col>
		</el-card>
		
		<el-card shadow="hover" style="margin-top: 20px;margin-bottom: 20px;" v-show="showPolicy">
			<div slot="header" class="clearfix">
			    <span style="font-size: 22px;">保单基本信息</span>
				<!-- <div style="float: right; padding: 3px 0">
					<el-button  type="text">提交</el-button>
				</div> -->
			</div>
			<el-form ref="form" :model="form" style="margin-left:10%;width:80%;min-width:600px;">
				<el-form-item>
					<el-col :span="3">保单号</el-col>
					<el-col :span="9">
						<el-input v-model="form.policyNo" disabled></el-input>
					</el-col>
					<el-col :span="3">投保单号</el-col>
					<el-col :span="9">
						<el-input v-model="form.proposalNo" disabled></el-input>
					</el-col>
					
				</el-form-item>
				<el-form-item>
					<el-col :span="3">保单类型</el-col>
					<el-col :span="9">
						<el-radio v-model="form.policyType" disabled label="01">个人</el-radio>
						<el-radio v-model="form.policyType" disabled label="02">团体</el-radio>
					</el-col>
					<el-col :span="3">险种</el-col>
					<el-col :span="9">
						<el-input v-model="form.insuranceName" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">被保险人名称</el-col>
					<el-col :span="9">
						<el-input v-model="form.insuredName" disabled></el-input>
					</el-col>
					<el-col :span="3">被保险人证件</el-col>
					<el-col :span="9">
						<el-input v-model="form.insuredCode" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">归属机构</el-col>
					<el-col :span="9">
						<el-input v-model="form.comName" disabled></el-input>
					</el-col>
					<el-col :span="3">保单状态</el-col>
					<el-col :span="9">
						<el-select v-model="form.policyStatus" disabled>
							<el-option value="0" label="未生效"></el-option>
							<el-option value="1" label="已生效"></el-option>
							<el-option value="2" label="已过期"></el-option>
							<el-option value="9" label="已退保"></el-option>
						</el-select>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">投保人名称</el-col>
					<el-col :span="9">
						<el-input v-model="form.appliName" disabled></el-input>
					</el-col>
					<el-col :span="3">投保人证件</el-col>
					<el-col :span="9">
						<el-input v-model="form.appliCode" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">主被保险人名称</el-col>
					<el-col :span="9">
						<el-input v-model="form.insuredName" disabled></el-input>
					</el-col>
					<el-col :span="3">主被保险人证件</el-col>
					<el-col :span="9">
						<el-input v-model="form.insuredCode" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">总保额</el-col>
					<el-col :span="9">
						<el-input v-model="form.sumAmount" disabled></el-input>
					</el-col>
					<el-col :span="3">总保费</el-col>
					<el-col :span="9">
						<el-input v-model="form.sumPremium" disabled></el-input>
					</el-col>
				</el-form-item>
			</el-form>
		</el-card>
	</section>
</template>

<script>
	import { getPolicyList } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		data() {
			return {
				filters: {
					policyNo: '',
					insuredCode: '',
					appliCode: ''
				},
				total: 0,
				page: 1,
				pageSize: 5,
				listLoading: false,
				showPolicy: false,
				tableData: [],
				form: {
					
				}
			}
		},
		methods: {
			//有效标志显示转换
			formatStatus(row, column) {
				let status = '';
				switch(row.policyStatus) {
					case '0' : status = '未生效';break;
					case '1' : status = '已生效';break;
					case '2' : status = '已过期';break;
					case '9' : status = '已退保';break;
					default: '未知'
				}
				return status;
			},
			formatStartTime(row, column) {
				return formatTimeToStr(row.startTime, 'yyyy-MM-dd hh:mm:ss');
			},
			formatEndTime(row, column) {
				return formatTimeToStr(row.endTime, 'yyyy-MM-dd hh:mm:ss');
			},
			handleSizeChange(val) {
				this.pageSize = val;
				this.getCommon();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getCommon();
			},
			selectPolicy(index, row) {
				this.form = row;
				this.showPolicy = true;
			},
			//获取字典列表
			getPolicy() {
				let params = {
					page: this.page,
					pageSize: this.pageSize,
					policyNo: this.filters.policyNo,
					insuredCode: this.filters.insuredCode,
					appliCode: this.filters.appliCode
				};
				this.listLoading = true;
				//NProgress.start();
				getPolicyList(params).then((res) => {
					this.tableData = res.data.records;
					this.total = res.data.total;
					this.listLoading = false;
					//NProgress.done();
				});
			},
			cellClass() {
				return 'text-align: center;'
			},
			headCellClass() {
				return 'background-color: #f5f7fa !important;text-align: center;'
			}
		},
		mounted() {
		}
	}
</script>

<style>
</style>
