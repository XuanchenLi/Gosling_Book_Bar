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
        <el-input v-model="ruleForm.account" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <template v-slot:label>
          <span>密码</span>
        </template>
        <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
        <el-button >注册</el-button>
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
        if (this.ruleForm.account !== '') {
          this.$refs.ruleForm.validateField('account')
        }
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value !== this.ruleForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
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
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
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
