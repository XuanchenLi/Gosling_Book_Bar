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
      <el-form-item prop="password" style="margin-bottom: 0">
        <template v-slot:label>
          <span>密码</span>
        </template>
        <el-input type="password" v-model="ruleForm.password" autocomplete="off" >
        </el-input>
      </el-form-item>
      <el-form-item style="margin: 0; height: 20px;">
        <span style="float: left;width: 40px">&ensp;</span>
        <el-checkbox style="float: left;" v-model="rememberMe">记住我</el-checkbox>
      </el-form-item>
      <el-form-item style="margin-top: 15px">
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
import { loginAPI } from '@/api/login'
import storage from '@/utils/storage'
import store from '@/store'
export default {
  name: 'LoginView',
  data () {
    return {
      rememberMe: false,
      ruleForm: {
        account: '',
        password: ''
      },
      rules: {
        account: [
          { require: true, trigger: 'blur' }
        ],
        password: [
          { require: true, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 设置cookie,登录成功之后进行调用 传入账号名，密码，和保存天数3个参数
    setCookie(name, pwd, exdays) {
      var exdate = new Date() // 获取时间
      exdate.setTime(exdate.getTime() + 24 * 60 * 60 * 1000 * exdays) // 保存的天数
      // 字符串拼接cookie
      window.document.cookie =
        'userName' + '=' + this.ruleForm.account + ';path=/;expires=' + exdate.toGMTString()
      window.document.cookie =
        'userPwd' + '=' + this.ruleForm.password + ';path=/;expires=' + exdate.toGMTString()
    },
    // 读取cookie 将用户名和密码回显到input框中
    getCookie() {
      if (document.cookie.length > 0) {
        this.rememberMe = true
        var arr = document.cookie.split('; ') //
        for (var i = 0; i < arr.length; i++) {
          var arr2 = arr[i].split('=') // 再次切割
          // 判断查找相对应的值
          if (arr2[0] === 'userName') {
            this.ruleForm.account = arr2[1]  // 保存到保存数据的地方即v-model
          } else if (arr2[0] === 'userPwd') {
            this.ruleForm.password = arr2[1]
          }
        }
      }
    },
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const account = {
            username: this.ruleForm.account,
            password: this.ruleForm.password
          }
          loginAPI(account).then(
            (res) => {
              console.log(res.data);
              let data = res.data
              if (data.success) {
                this.$message.success('登录成功')
                //根据store中set_token方法将token保存至localStorage/sessionStorage中，data["Authentication-Token"]，获取token的value值
                this.$store.commit('set_token', data.data['token'])
                storage.set("token", data.data['token'])
                console.log(storage.get("token"))
                if (this.rememberMe) {
                  this.setCookie()
                }
                this.$router.push({path: '/home'})
              }else {
                this.$message.error(data.message)
              }
            }
          ).catch(
            (error) => {
              this.$message.error(error.message)
              // console.log(error)
              return false
            }
          )
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  },
  mounted: function () {
    document.title = '登录'
    this.getCookie();
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
