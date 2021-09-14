<template>
	<section>
		<el-card shadow="hover" style="margin-bottom: 20px;">
			<!--列表-->
			<el-table :data="tableData" highlight-current-row v-loading="loading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
				<el-table-column prop="settlementNo" label="理算书号" min-width="120" sortable>
				</el-table-column>
				<el-table-column prop="settlementType" label="理算类型" min-width="120" :formatter="formatSettlementType">
				</el-table-column>
				<el-table-column prop="sumRealPay" label="理算金额" min-width="100">
				</el-table-column>
				<el-table-column prop="underwriteFlag" label="审核状态" min-width="120" :formatter="formatUnderwriteFlag">
				</el-table-column>
				<el-table-column prop="underwriteDate" label="核赔通过时间" min-width="100" :formatter="formatUnderwriteDate">
				</el-table-column>
				<el-table-column prop="payStatus" label="是否支付" width="80" :formatter="formatPayStatus">
				</el-table-column>
				<el-table-column prop="payDate" label="支付时间" width="100" :formatter="formatPayDate">
				</el-table-column>
				<el-table-column label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" @click="handleEdit(scope.$index, scope.row)">
							<span v-if="scope.row.underwriteFlag == '0'">编辑</span>
							<span v-if="scope.row.underwriteFlag != '0'">查看</span>
						</el-button>
					</template>
				</el-table-column>
			</el-table>
					
			<el-col :span="24" class="toolbar" style="background: #fff;">
				<el-select size="small" v-model="settlementType" style="float: left;">
					<el-option value="P" label="赔款理算"></el-option>
					<el-option value="F" label="费用理算"></el-option>
					<el-option value="Y" label="预赔理算"></el-option>
				</el-select>
				<el-button size="small" type="primary" style="float: left;margin-left: 20px;" :disabled="casedis" @click="handleAdd">新增</el-button>
				<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5, 10, 20]" :total="total" style="float:right;">
				</el-pagination>
			</el-col>
		</el-card>
	</section>
</template>

<script>
	import { getSettlementList,checkSettlementForAdd } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		props: ['reportNo','caseFlag'],
		data() {
			return {
				settlementType: 'P',
				total: 0,
				page: 1,
				pageSize: 5,
				loading: false,
				tableData: [],
				casedis: false
			}
		},
		methods: {
			formatSettlementType(row, column) {
				let text = '';
				switch(row.settlementType) {
					case 'P': text = '赔款理算';break;
					case 'F': text = '费用理算';break;
					case 'Y': text = '预赔理算';break;
					default: text = '未知';
				}
				return text;
			},
			formatPayStatus(row, column) {
				return row.payStatus == 1 ? '已支付' : row.payStatus == 0 ? '未支付' : '未知';
			},
			formatPayDate(row, column) {
				if(row.payDate) {
					return formatTimeToStr(row.payDate, 'yyyy-MM-dd');
				} else {
					return '';
				}
			},
			formatUnderwriteDate(row, column) {
				if(row.underwriteDate) {
					return formatTimeToStr(row.underwriteDate, 'yyyy-MM-dd hh:mm:ss');
				} else {
					return '';
				}
			},
			formatUnderwriteFlag(row, column) {
				let text = '';
				switch(row.underwriteFlag) {
					case '0': text = '初始化/退回';break;
					case '1': text = '审核中';break;
					case '2': text = '审核完毕';break;
					case '-1': text = '已注销';break;
					default: text = '未知';
				}
				return text;
			},
			handleSizeChange(val) {
				this.pageSize = val;
				this.getSettlement();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.getSettlement();
			},
			//获取理算书列表
			getSettlement() {
				let params = {
					page: this.page,
					pageSize: this.pageSize,
					reportNo: this.reportNo
				};
				this.loading = true;
				//NProgress.start();
				getSettlementList(params).then((res) => {
					if(res) {
						this.tableData = res.data.records;
						this.total = res.data.total;
						setTimeout(() => {
							this.loading = false;
						},500);
					}
					//NProgress.done();
				});
			},
			cellClass () {
				return 'text-align: center;'
			},
			headCellClass () {
				return 'background-color: #f5f7fa !important;text-align: center;'
			},
			handleAdd() {
				let params = {
					reportNo:this.reportNo,
					settlementType: this.settlementType
				}
				checkSettlementForAdd(params).then((res) => {
					if(res.data.data.status == '0') {
						this.$message.warning(res.data.data.msg);
					} else {
						let routeUrl = this.$router.resolve({
							name: 'SettlementDetail',
							query: {
								reportNo:this.reportNo,
								settlementType: this.settlementType
							}
						});
						window.open(routeUrl.href, '_blank');
					}
				});
			},
			handleEdit(index,row) {
				let routeUrl = this.$router.resolve({
					name: 'SettlementDetail',
					query: {
						reportNo:row.reportNo,
						settlementNo:row.settlementNo,
						settlementType: row.settlementType
					}
				});
				window.open(routeUrl.href, '_blank');
			}
		},
		mounted() {
			this.getSettlement();
			if(this.caseFlag == '04' || this.caseFlag == '01') {
				this.casedis = true;
			}
		}
	}
</script>

<style>
</style>
