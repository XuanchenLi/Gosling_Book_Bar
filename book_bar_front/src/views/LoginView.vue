<template>
  <div class="LoginView">
    <div class="login-warp" style="border-radius: 10px">
    <el-row type="flex" justify="center">
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm">
      <h2>管理员登录</h2>
      <hr>
      <el-form-item prop="account">
        <template v-slot:label>
          <span>账号</span>
        </template>
        <el-input v-model="ruleForm.account" autocomplete="on"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <template v-slot:label>
          <span>密码</span>
        </template>
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" >
        </el-input>
      </el-form-item>
      <el-form-item>
        <p></p>
        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
        <el-button type="primary" @click="this.$router.push('/register')">注册</el-button>
      </el-form-item>
    </el-form>
    </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginView',
  data () {
    const validateAccount = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入账号'))
      } else {
        if (this.ruleForm.account.trim() === '') {
          callback(new Error('非法格式'))
        }
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (this.ruleForm.password !== '') {
        const reg = /^(?!\d+$)(?![a-zA-Z]+$)(?![@$!%*#?&.()]+$)[\dA-Za-z@$!%*#?&￥{}|^~:;\-=+/\\.()]{6,20}$/
        if (!reg.test(this.ruleForm.password)) {
          callback(new Error('密码为6-20位，且同时包含数字和字符'))
        }
        callback()
      }
    }
    return {
      ruleForm: {
        account: '',
        password: ''
      },
      rules: {
        account: [
          { validator: validateAccount, trigger: 'blur' }
        ],
        password: [
          { validator: validatePassword, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!')
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>
<style scoped>

.LoginView{
  width: 100%;
  height: auto;
  background: url("../assets/login_back.JPG");
  background-size: cover;
  overflow: hidden;
  top: 0;
  left: 0;
}
.login-warp{
  background-size: cover;
  width: 400px;
  height: 300px;
  margin: 200px auto;
  overflow: hidden;
  padding-top: 10px;
  line-height: 40px;
  background-color: rgba(255, 255, 255, 0.8);
}
</style>
