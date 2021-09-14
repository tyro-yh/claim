<template>
	<section>
		<el-table :data="lossCharges" v-loading="loading" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
			<el-table-column label="条款-险别" min-width="180">
				<template slot-scope="scope">
					<el-select v-model="scope.row.clauseCode" disabled>
						<el-option
						  v-for="item in claimKindF"
						  :key="item.clauseCode"
						  :label="item.clauseName"
						  :value="item.clauseCode">
						</el-option>
					</el-select>
				</template>
			</el-table-column>
			<el-table-column label="费用类型" min-width="180">
				<template slot-scope="scope">
					<el-select v-model="scope.row.costType" disabled>
						<el-option value="1" label="差旅费"></el-option>
						<el-option value="2" label="律师费"></el-option>
						<el-option value="3" label="公估费"></el-option>
						<el-option value="4" label="诉讼费"></el-option>
						<el-option value="5" label="专家费"></el-option>
						<el-option value="6" label="其他"></el-option>
					</el-select>
				</template>
			</el-table-column>
			<el-table-column prop="remark" label="支付说明" width="200">
			</el-table-column>
			<el-table-column label="支付对象" width="200">
				<template slot-scope="scope">
					<el-select v-model="scope.row.payObject" disabled>
						<el-option value="1" label="员工"></el-option>
						<el-option value="2" label="律师行"></el-option>
						<el-option value="3" label="公估公司"></el-option>
						<el-option value="4" label="其他"></el-option>
					</el-select>
				</template>
			</el-table-column>
			<el-table-column prop="sumLoss" label="定损金额" width="140">
			</el-table-column>
			<el-table-column prop="sumRealPay" label="赔付金额" width="140">
			</el-table-column>
		</el-table>
	</section>
</template>

<script>
	export default {
		props: ['lossCharges','claimKindF'],
		data() {
			return {
			}
		},
		methods: {
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
