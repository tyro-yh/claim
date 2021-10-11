<template>
  <section>
    <template v-for="(row,index) in lossPerson">
      <el-descriptions class="margin-top" :column="3" border :labelStyle="{'width':'150px'}" :contentStyle="{'min-width':'150px'}">
        <template slot="title">
          <span style="font-size: 14px;color: #909399;">人伤损失{{index+1}}</span>
        </template>
        <el-descriptions-item label="姓名">
          <el-input v-model="row.personName" disabled></el-input>
        </el-descriptions-item>
        <el-descriptions-item label="性别">
          <span v-if="row.sex == '1'">男</span>
          <span v-if="row.sex == '2'">女</span>
          <span v-if="row.sex == '0'">未知</span>
        </el-descriptions-item>
        <el-descriptions-item label="年龄">{{row.age}}</el-descriptions-item>
        <el-descriptions-item label="证件类型">
          <span  v-if="row.identifyType == '01'">身份证</span>
          <span  v-if="row.identifyType == '02'">户口薄</span>
          <span  v-if="row.identifyType == '03'">护照</span>
          <span  v-if="row.identifyType == '04'">军官证</span>
          <span  v-if="row.identifyType == '05'">驾驶证</span>
          <span  v-if="row.identifyType == '99'">其他</span>
        </el-descriptions-item>
        <el-descriptions-item label="证件号码">{{row.identifyNo}}</el-descriptions-item>
        <el-descriptions-item label="合计金额">{{row.sumAmt}}</el-descriptions-item>
      </el-descriptions>
      <el-table :data="row.lossPersonFee" v-loading="loading" highlight-current-row style="width: 100%;" border :cell-style="cellClass" :header-cell-style="headCellClass">
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
        		<el-select v-model="scope.row.itemCode" v-if="scope.row.clauseCode" disabled>
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
        <el-table-column label="费用名目" min-width="140">
          <template slot-scope="scope">
            <el-select v-model="scope.row.feeTypeCode" disabled>
              <el-option v-for="item in feeTypeList"
               :key="item.value"
               :label="item.label"
               :value="item.value">
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
    </template>
  </section>
</template>

<script>
  export default {
  	props: ['lossPerson','claimKindP'],
  	data() {
  		return {
        feeTypeList: [
          {label:"医药费" ,value:"1"},
          {label:"后续治疗费" ,value:"2"},
          {label:"伙食补助费" ,value:"3"},
          {label:"整容费" ,value:"4"},
          {label:"营养费" ,value:"5"},
          {label:"其他" ,value:"6"},
        ],
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
