import axios from 'axios';
import router from '@/router.js';

let base = '/claim';

axios.interceptors.request.use(function (config) {
	let token = '';
	let user = JSON.parse(sessionStorage.getItem('user'));
	if (user) {
		token = user.token;
	}
	config.headers.common['token'] = token;
	return config;
}, function (error) {
	return Promise.reject(error);
});

// 添加响应拦截器
axios.interceptors.response.use((response) => {
	if (response.headers['logout']) {
		sessionStorage.removeItem('user');
		router.push({name:'Login',params:{msg:'登录过期,请重新登录'}});
	} else {
		return response;
	}
}, function (error) {
	return Promise.reject(error);
});

export const removeRedis = params => { return axios.post(`${base}/redis/remove`, params); };

export const requestLogin = params => { return axios.post(`${base}/login`, params).then(res => res.data); };

export const getCompanyList = params => { return axios.post(`${base}/dictionary/companyDict`, params); };

export const getCompanyLevel = params => { return axios.post(`${base}/dictionary/companyLevel`, params); };

export const getUserList = params => { return axios.post(`${base}/dictionary/userDict`, params); };

export const editCompany = params => { return axios.post(`${base}/dictionary/editCompany`, params); };

export const addCompany = params => { return axios.post(`${base}/dictionary/addCompany`, params); };

export const checkComCode = params => { return axios.post(`${base}/dictionary/checkComCode`, params); };

export const editUser = params => { return axios.post(`${base}/dictionary/editUser`, params); }

export const getCommonList = params => { return axios.post(`${base}/dictionary/commonDict`, params); };

export const getCommonDataType = params => { return axios.post(`${base}/dictionary/commonDataType`); };

export const getDamageCodes = params => { return axios.post(`${base}/dictionary/getDamageCodes`); };

export const getProvinceList = params => { return axios.post(`${base}/dictionary/getProvinceList`); };

export const getCityList = params => { return axios.post(`${base}/dictionary/getCityList`, params); };

export const getCountyCodeList = params => { return axios.post(`${base}/dictionary/getCountyCodeList`, params); };

export const getCertifyList = params => { return axios.post(`${base}/dictionary/getCertifyList`); };

export const selectByDataType = params => { return axios.post(`${base}/dictionary/selectByDataType`, params); };

export const editCommon = params => { return axios.post(`${base}/dictionary/editCommon`, params); };

export const addCommon = params => { return axios.post(`${base}/dictionary/addCommon`, params); };

export const getMyTask = params => { return axios.post(`${base}/workflow/myTask`, params); };

export const initTaskStatus = params => { return axios.post(`${base}/workflow/initTaskStatus`, params); };

export const insuranceScale = params => { return axios.post(`${base}/workflow/insuranceScale`); };

export const selectTodoList = params => { return axios.post(`${base}/common/selectTodoList`, params); };

export const addTodo = params => { return axios.post(`${base}/common/addTodo`, params); };

export const delTodo = params => { return axios.post(`${base}/common/delTodo`, params); };

export const editTodo = params => { return axios.post(`${base}/common/editTodo`, params); };

export const selectLoginHis = params => { return axios.post(`${base}/common/selectLoginHis`, params); };

export const getWorkFlowList = params => { return axios.post(`${base}/workflow/getWorkFlowList`, params); };

export const getPolicyList = params => { return axios.post(`${base}/policy/getPolicyList`, params); };

export const initPolicyInfo = params => { return axios.post(`${base}/policy/initPolicyInfo`, params); };

export const saveReport = params => { return axios.post(`${base}/report/saveReport`, params); };

export const getReport = params => { return axios.post(`${base}/report/getReport`, params); };

export const initReportInfo = params => { return axios.post(`${base}/report/initReportInfo`, params); };

export const initCaseFlag = params => { return axios.post(`${base}/report/initCaseFlag`, params); };

export const initClaimInfo = params => { return axios.post(`${base}/claim/initClaimInfo`, params); };

export const saveClaim = params => { return axios.post(`${base}/claim/saveClaim`, params); };

export const submitClaim = params => { return axios.post(`${base}/claim/submitClaim`, params); };

export const applyEndCase = params => { return axios.post(`${base}/endcase/applyEndCase`, params); };

export const initEndCaseInfo = params => { return axios.post(`${base}/endcase/initEndCaseInfo`, params); };

export const saveProcess = params => { return axios.post(`${base}/process/saveProcess`, params); };

export const getProcessList = params => { return axios.post(`${base}/process/getProcessList`, params); };

export const initApproveInfo = params => { return axios.post(`${base}/approve/initApproveInfo`, params); };

export const saveApprove = params => { return axios.post(`${base}/approve/saveApprove`, params); };

export const submitApprove = params => { return axios.post(`${base}/approve/submitApprove`, params); };

export const getApproveForSettlement = params => { return axios.post(`${base}/approve/getApproveForSettlement`, params); };

export const getSettlementList = params => { return axios.post(`${base}/settlement/getSettlementList`, params); };

export const initSettlementInfo = params => { return axios.post(`${base}/settlement/initSettlementInfo`, params); };

export const initSettlementForUndwrt = params => { return axios.post(`${base}/settlement/initSettlementForUndwrt`, params); };

export const initSettlementContent = params => { return axios.post(`${base}/settlement/initSettlementContent`, params); };

export const saveSettlement = params => { return axios.post(`${base}/settlement/saveSettlement`, params); };

export const submitSettlement = params => { return axios.post(`${base}/settlement/submitSettlement`, params); };

export const cancelSettlement = params => { return axios.post(`${base}/settlement/cancelSettlement`, params); };

export const checkSettlementForAdd = params => { return axios.post(`${base}/settlement/checkSettlementForAdd`, params); };

export const checkSettlementForSave = params => { return axios.post(`${base}/settlement/checkSettlementForSave`, params); };

export const checkSettlementForSubmit = params => { return axios.post(`${base}/settlement/checkSettlementForSubmit`, params); };

export const checkAccountNo = params => { return axios.post(`${base}/common/checkAccountNo`, params); };

export const initPayeeList = params => { return axios.post(`${base}/payee/initPayeeList`, params); };

export const savePayee = params => { return axios.post(`${base}/payee/savePayee`, params); };

export const initPaymentList = params => { return axios.post(`${base}/payment/initPaymentList`, params); };

export const getCollectionList = params => { return axios.post(`${base}/collection/getCollectionList`, params); };

export const collectionUpload = params => { return axios.post(`${base}/collection/collectionUpload`, params,{headers: {'Content-Type': 'multipart/form-data'}}); };

export const fileDownload = params => { return axios.post(`${base}/collection/fileDownload`, params,{responseType: 'blob'}); };