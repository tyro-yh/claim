<template>
	<section>
		<el-card shadow="hover" style="margin-bottom: 20px;">
			<div slot="header" class="clearfix">
			    <span style="font-size: 22px;">报案基本信息</span>
				<!-- <div style="float: right; padding: 3px 0">
					<el-button  type="text">提交</el-button>
				</div> -->
			</div>
			<el-form ref="form" :model="form" style="margin-left:20%;width:60%;min-width:600px;">
				<el-form-item>
					<el-col :span="3">报案号</el-col>
					<el-col :span="9">
						<el-input v-model="form.reportNo" disabled></el-input>
					</el-col>
					<el-col :span="3">报案时间</el-col>
					<el-col :span="9">
						<el-input v-model="form.reportTime" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">保单号</el-col>
					<el-col :span="9">
						<el-input v-model="form.policyNo" disabled></el-input>
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
					<el-col :span="3">保单类型</el-col>
					<el-col :span="9">
						<el-radio v-model="form.policyType" disabled label="01">个人</el-radio>
						<el-radio v-model="form.policyType" disabled label="02">团体</el-radio>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">出险原因</el-col>
					<el-col :span="9" style="display: flex;">
						<el-select v-model="form.damageCode" disabled>
							<el-option
							  v-for="item in damageOptions"
							  :key="item.value"
							  :label="item.label"
							  :value="item.value">
							</el-option>
						</el-select>
					</el-col>
					<el-col :span="3">出险时间</el-col>
					<el-col :span="9">
						<el-date-picker disabled
						  format="yyyy-MM-dd HH"	
						  v-model="form.damageTime"
						  type="datetime"
						  placeholder="选择日期时间">
						</el-date-picker>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">出险地点</el-col>
					<el-col :span="3" style="display: flex;">
						<el-input v-model="form.province" disabled></el-input>
					</el-col>
					<el-col :span="1">-</el-col>
					<el-col :span="3">
						<el-input v-model="form.city" disabled></el-input>
					</el-col>
					<el-col :span="1">-</el-col>
					<el-col :span="3">
						<el-input v-model="form.countyCode" disabled></el-input>
					</el-col>
					<el-col :span="1">-</el-col>
					<el-col :span="9">
						<!-- <el-autocomplete
						style="width: 100%;"
						  v-model="form.damageAddress"
						  :fetch-suggestions="queryAddress"
						  placeholder="详细地址"
						  @select="handleDamageCode"
						></el-autocomplete> -->
						<el-input v-model="form.damageAddress" maxlength="100" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">报损金额</el-col>
					<el-col :span="9" style="display: flex;">
						<el-input-number v-model="form.reportLoss" :precision="2" :min="0" label="报损金额" disabled></el-input-number>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">报案人</el-col>
					<el-col :span="9">
						<el-input v-model="form.reportorName" disabled></el-input>
					</el-col>
					<el-col :span="3">报案人电话</el-col>
					<el-col :span="9">
						<el-input v-model="form.reportorPhone" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">联系人</el-col>
					<el-col :span="9">
						<el-input v-model="form.linkerName" disabled></el-input>
					</el-col>
					<el-col :span="3">联系人电话</el-col>
					<el-col :span="9">
						<el-input v-model="form.linkerPhone" disabled></el-input>
					</el-col>
				</el-form-item>
				<el-form-item>
					<el-col :span="3">出险经过描述</el-col>
					<el-col :span="21">
						<el-input type="textarea" v-model="form.remark" maxlength="500" show-word-limit disabled></el-input>
					</el-col>
				</el-form-item>
				<!-- <el-form-item>
					<el-button type="primary" @click='handleSubmit'>提交</el-button>
					<el-button @click.native.prevent>取消</el-button>
				</el-form-item> -->
			</el-form>
		</el-card>
	</section>
</template>

<script>
	import { getReport,getDamageCodes,getProvinceList,getCityList,getCountyCodeList } from '@/api/api';
	import { formatTimeToStr} from '@/common/js/date.js';
	export default {
		props: ['reportNo'],
		data() {
			return {
				provinceName: '',
				cityName: '',
				countyCodeName: '',
				form: {
					reportNo: '',
					reportTime: '',
					policyNo: '',
					insuranceName: '',
					insuranceCode: '',
					insuredName: '',
					insuredCode: '',
					policyType: '',
					comCode: '',
					comName: '',
					reportLoss: 0,
					damageCode: '',
					damageTime: '',
					province: '',
					city: '',
					countyCode: '',
					damageAddress: '',
					reportorName: '',
					reportorPhone: '',
					linkerName: '',
					linkerPhone: '',
					remark: ''
				},
				damageOptions: [],
				provinceRestaurants: [],
				cityRestaurants: [],
				countyCodeRestaurants: [],
				tableData: []
			}
		},
		methods: {
			queryProvince(queryString, cb) {
				let restaurants = this.provinceRestaurants;
				let results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
				// 调用 callback 返回建议列表的数据
				cb(results);
			},
			queryCity(queryString, cb) {
				let restaurants = this.cityRestaurants;
				let results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
				// 调用 callback 返回建议列表的数据
				cb(results);
			},
			queryCountyCode(queryString, cb) {
				let restaurants = this.countyCodeRestaurants;
				let results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
				// 调用 callback 返回建议列表的数据
				cb(results);
			},
			createFilter(queryString) {
				return (restaurant) => {
				  return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
				};
			},
			selectProvince(item) {
				this.form.province = item.label;
				this.provinceName = item.value;
				this.getCity(this.form.province);
			},
			selectCity(item) {
				this.form.city = item.label;
				this.cityName = item.value;
				this.getCountyCode(this.form.city);
			},
			selectCountyCode(item) {
				this.form.countyCode = item.label;
				this.countyCodeName = item.value;
			},
			getProvince() {
				getProvinceList().then((res) => {
					this.provinceRestaurants = res.data.data;
				});
			},
			getCity(preCode) {
				let params = {
					preCode: preCode
				};
				getCityList(params).then((res) => {
					this.cityRestaurants = res.data.data;
				});
			},
			getCountyCode(preCode) {
				let params = {
					preCode: preCode
				};
				getCountyCodeList(params).then((res) => {
					this.countyCodeRestaurants = res.data.data;
				});
			},
			getDamageOptions() {
				getDamageCodes().then((res) => {
					this.damageOptions = res.data.data;
				});
			},
			getReportInfo() {
				let params = {
					reportNo: this.reportNo
				}
				getReport(params).then((res) => {
					this.form = res.data.data
				})
			}
		},
		mounted() {
			this.getReportInfo();
			this.getDamageOptions();
			this.getProvince();
		}
	}
</script>

<style>
	.labelTd {
		width: 100px;
		text-align: right;
	}
	
	.el-autocomplete-suggestion {
		width: 160px !important;
	}
</style>
