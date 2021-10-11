<template>
	<section>
		<el-table :data="lossProps" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
			<el-table-column label="条款-险别" min-width="180">
				<template slot-scope="scope">
					<el-select v-model="scope.row.clauseCode" disabled>
						<el-option
						  v-for="item in claimKindP"
						  :key="item.clauseCode"
						  :label="item.clauseName"
						  :value="item.clauseCode">
						</el-option>
					</el-select>
				</template>
			</el-table-column>
			<el-table-column label="损失类型" min-width="180">
				<template slot-scope="scope">
					<el-select v-model="scope.row.itemCode" disabled>
						<el-option
						  v-for="item in claimKindP"
						  v-if="item.clauseCode == scope.row.clauseCode"
						  :key="item.itemCode"
						  :label="item.itemName"
						  :value="item.itemCode">
						</el-option>
					</el-select>
				</template>
			</el-table-column>
			<el-table-column prop="amount" label="保险责任限额" min-width="140">
			</el-table-column>
			<el-table-column prop="sumLoss" label="定损金额" width="140">
			</el-table-column>
			<el-table-column prop="sumLossChecked" label="核损金额" width="140">
			</el-table-column>
			<el-table-column prop="claimRate" label="责任比例(%)" width="100">
			</el-table-column>
			<el-table-column prop="deductAddRate" label="免赔率(%)" width="100">
			</el-table-column>
			<el-table-column prop="deductAddAmt" label="免赔额" width="140">
			</el-table-column>
			<el-table-column prop="sumAmt" label="理算金额" width="140">
			</el-table-column>
			<el-table-column prop="sumRealPay" label="赔付金额" width="140">
			</el-table-column>
		</el-table>
	</section>
</template>

<script>
	export default {
		props: ['lossProps','claimKindP'],
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
