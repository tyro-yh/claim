<template>
  <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-position="left" label-width="0px" class="demo-ruleForm login-container">
    <h3 class="title">系统登录</h3>
    <el-form-item prop="account">
      <el-input type="text" v-model="ruleForm.account" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="checkPass">
      <el-input type="password" v-model="ruleForm.checkPass" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <el-checkbox v-model="checked" checked class="remember">记住密码</el-checkbox>
    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:100%;" @click.native.prevent="handleSubmit" :loading="logining">登录</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import { requestLogin } from '@/api/api';
  //import NProgress from 'nprogress'
  export default {
	props: ['msg'],
    data() {
      return {
        logining: false,
        ruleForm: {
          account: '',
          checkPass: ''
        },
        rules: {
          account: [
            { required: true, message: '请输入账号', trigger: 'blur' },
          ],
          checkPass: [
            { required: true, message: '请输入密码', trigger: 'blur' },
          ]
        },
        checked: true
      };
    },
    methods: {
      handleSubmit(ev) {
        var _this = this;
        this.$refs.ruleForm.validate((valid) => {
          if (valid) {
            //_this.$router.replace('/table');
            this.logining = true;
            //NProgress.start();
            var loginParams = { userCode: this.ruleForm.account, passWord: this.ruleForm.checkPass };
            requestLogin(loginParams).then(data => {
              this.logining = false;
              //NProgress.done();
              if (data.status !== 200) {
                this.$message({
                  message: data.msg,
                  type: 'error'
                });
              } else {
                localStorage.setItem('user',JSON.stringify(data.data));
                // sessionStorage.setItem('user', JSON.stringify(data.data));
                this.$router.push({ path: '/main' });
              }
            });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      }
    },
	mounted() {
		if(this.msg) {
			this.$message.error(this.msg);
		}
	}
  }

</script>

<style scoped>
.login-container {
	 -webkit-border-radius: 5px;
	 border-radius: 5px;
	 -moz-border-radius: 5px;
	 background-clip: padding-box;
	 margin: 180px auto;
	 width: 350px;
	 padding: 35px 35px 15px 35px;
	 background: #fff;
	 border: 1px solid #eaeaea;
	 box-shadow: 0 0 25px #cac6c6;
}
 .login-container .title {
	 margin: 0px auto 40px auto;
	 text-align: center;
	 color: #505458;
}
 .login-container .remember {
	 margin: 0px 0px 35px 0px;
}
</style>
