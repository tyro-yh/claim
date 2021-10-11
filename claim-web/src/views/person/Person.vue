<template>
  <section>
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <!--列表-->
      <el-table :data="tableData" highlight-current-row v-loading="loading" style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
      	<el-table-column prop="personName" label="人员名称" min-width="100">
      	</el-table-column>
      	<el-table-column prop="identifyNo" label="证件号码" min-width="120">
      	</el-table-column>
      	<el-table-column prop="sumAmt" label="人伤合计金额" min-width="100">
      	</el-table-column>
      	<el-table-column prop="underwriteFlag" label="审核状态" min-width="120" :formatter="formatUnderwriteFlag">
      	</el-table-column>
      	<el-table-column prop="underwriteDate" label="人伤核损时间" min-width="100" :formatter="formatUnderwriteDate">
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
      	<el-button size="small" type="primary" style="float: left;margin-left: 20px;" :disabled="casedis" @click="handleAdd">新增</el-button>
      	<el-pagination layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange" @current-change="handleCurrentChange" :page-size="5" :page-sizes="[5, 10, 20]" :total="total" style="float:right;">
      	</el-pagination>
      </el-col>
    </el-card>
  </section>
</template>

<script>
  import { getPersonList } from '@/api/api';
  import { formatTimeToStr} from '@/common/js/date.js';
  export default {
  	props: ['reportNo','caseFlag'],
  	data() {
  		return {
  			total: 0,
  			page: 1,
  			pageSize: 5,
  			loading: false,
  			tableData: [],
  			casedis: false
  		}
  	},
  	methods: {
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
  		formatUnderwriteDate(row, column) {
  			if(row.underwriteDate) {
  				return formatTimeToStr(row.underwriteDate, 'yyyy-MM-dd hh:mm:ss');
  			} else {
  				return '';
  			}
  		},
  		handleSizeChange(val) {
  			this.pageSize = val;
  			this.getSettlement();
  		},
  		handleCurrentChange(val) {
  			this.page = val;
  			this.getSettlement();
  		},
  		//获取人伤跟踪列表
  		getPerson() {
  			let params = {
  				page: this.page,
  				pageSize: this.pageSize,
  				reportNo: this.reportNo
  			};
  			this.loading = true;
  			getPersonList(params).then((res) => {
  				if(res) {
  					this.tableData = res.data.records;
  					this.total = res.data.total;
  					setTimeout(() => {
  						this.loading = false;
  					},500);
  				}
  			});
  		},
  		cellClass () {
  			return 'text-align: center;'
  		},
  		headCellClass () {
  			return 'background-color: #f5f7fa !important;text-align: center;'
  		},
      handleAdd() {
      	let routeUrl = this.$router.resolve({
      		name: 'PersonDetail',
      		query: {
      			reportNo:this.reportNo
      		}
      	});
      	window.open(routeUrl.href, '_blank');
      },
      handleEdit(index,row) {
      	let routeUrl = this.$router.resolve({
      		name: 'PersonDetail',
      		query: {
      			reportNo:row.reportNo,
      			personId:row.id
      		}
      	});
      	window.open(routeUrl.href, '_blank');
      }
  	},
  	mounted() {
  		this.getPerson();
  		if(this.caseFlag == '04' || this.caseFlag == '01') {
  			this.casedis = true;
  		}
  	}
  }
</script>

<style>
</style>
