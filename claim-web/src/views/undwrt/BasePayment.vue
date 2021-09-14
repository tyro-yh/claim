<template>
	<section>
		<el-table :data="paymentList" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
			<el-table-column prop="payeeInfo.payeeKind" label="客户类型" min-width="120" :formatter="formatPayeeKind">
			</el-table-column>
			<el-table-column prop="payeeInfo.payeeType" label="单位/个人" min-width="120" :formatter="formatPayeeType">
			</el-table-column>
			<el-table-column prop="payeeInfo.payeeName" label="领款人名称" min-width="120">
			</el-table-column>
			<el-table-column prop="payeeInfo.payeePhone" label="联系方式" width="120">
			</el-table-column>
			<el-table-column prop="payeeInfo.certifyType" label="证件类型" width="120" :formatter="formatCertifyType">
			</el-table-column>
			<el-table-column prop="payeeInfo.certifyNo" label="证件号" width="160">
			</el-table-column>
			<el-table-column prop="payeeInfo.accountType" label="收款方式" width="120" :formatter="formatAccountType">
			</el-table-column>
			<el-table-column prop="payeeInfo.accountNo" label="帐号/卡号" width="160">
			</el-table-column>
			<el-table-column prop="amount" label="领款金额" width="120">
			</el-table-column>
			<el-table-column prop="remark" label="领款备注" width="160">
			</el-table-column>
		</el-table>
	</section>
</template>

<script>
	export default {
		props: ['paymentList'],
		data() {
			return {
			}
		},
		methods: {
			formatPayeeKind(row, column) {
				let payeeKind = '';
				let code = '';
				if(row.payeeInfo) {
					code = row.payeeInfo.payeeKind;
				} else {
					code = row.payeeKind;
				}
				switch(code) {
					case '01': payeeKind = '被保险人';break;
					case '02': payeeKind = '委托人';break;
					case '03': payeeKind = '受益人';break;
					case '04': payeeKind = '其他';break;
					default: payeeKind = '未知'
				}
				return payeeKind;
			},
			formatPayeeType(row, column) {
				let payeeType = '';
				let code = '';
				if(row.payeeInfo) {
					code = row.payeeInfo.payeeType;
				} else {
					code = row.payeeType;
				}
				switch(code) {
					case '1': payeeType = '个人';break;
					case '2': payeeType = '单位';break;
					default: payeeType = '未知'
				}
				return payeeType;
			},
			formatCertifyType(row, column) {
				let certifyType = '';
				let code = '';
				if(row.payeeInfo) {
					code = row.payeeInfo.certifyType;
				} else {
					code = row.certifyType;
				}
				switch(code) {
					case '01': certifyType = '组织机构代码证';break;
					case '02': certifyType = '身份证';break;
					default: certifyType = '未知'
				}
				return certifyType;
			},
			formatAccountType(row, column) {
				let accountType = '';
				let code = '';
				if(row.payeeInfo) {
					code = row.payeeInfo.accountType;
				} else {
					code = row.accountType;
				}
				switch(code) {
					case '1': accountType = '银行卡';break;
					case '2': accountType = '支付宝';break;
					case '3': accountType = '微信';break;
					default: accountType = '未知'
				}
				return accountType;
			},
			cellClass() {
				return 'text-align: center;'
			},
			headCellClass() {
				return 'background-color: #f5f7fa !important;text-align: center;'
			},
		},
		mounted() {
		}
	}
</script>

<style>
</style>
