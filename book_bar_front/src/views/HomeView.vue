<template>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes" />
  <div class="home" style="height: 600px; background-color: #f3f4f7">
    <el-container class="main-header">
      <el-header style="text-align: right; font-size: 12px">
        <div class="header-title">
          <el-icon>
            <Menu />
          </el-icon>
          <span style="display: block; float: right">小鹅书吧员工管理系统</span>
        </div>
        <div class="profile-dropdown" style="float: right;">
          <el-dropdown>
            <span class="el-dropdown-link">
              <el-icon>
                <avatar />
              </el-icon>
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="viewProfile">修改个人信息</el-dropdown-item>
                <el-dropdown-item>修改密码</el-dropdown-item>
                <el-dropdown-item @click="logout();">退出账户</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div class="clock" style="color:white; float: right">
          {{nowTime}}
        </div>
      </el-header>
    </el-container>
    <el-container class="main-side-bar" >
      <el-aside width="200px" style="overflow: hidden;" >
        <el-scrollbar height="500px">
          <el-menu :default-openeds="['1']"
                   :default-active="this.$router.path"
                   :router="true">
            <el-submenu index="1">
              <template #title>
                <el-icon><user /></el-icon>&ensp;员工管理
              </template>
              <el-menu-item index="/home/searchEmployee">员工管理</el-menu-item>
              <el-menu-item index="/home/department">部门管理</el-menu-item>
            </el-submenu>
            <el-submenu index="2">
              <template #title>
                <el-icon><money /></el-icon>&ensp;财务系统
              </template>
              <el-menu-item-group>
                <template #title>财务统计</template>
                <el-menu-item index="/home/profit">盈利统计</el-menu-item>
                <el-menu-item index="/home/salary">薪资统计</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="员工工资">
                <el-menu-item index="/home/personalSalary">薪资查询</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="3">
              <template #title>
                <el-icon><setting /></el-icon>&ensp;系统管理
              </template>
              <el-menu-item index="/home/authority">权限控制</el-menu-item>
            </el-submenu>
          </el-menu>
        </el-scrollbar>
        <div style="height: 60px; background-color: #24406f; margin-top: 0">
        </div>
      </el-aside>
      <router-link to="/home/searchEmployee"></router-link>
      <router-view></router-view>
    </el-container>

    <el-dialog  v-model="openProfileDialog" title="个人信息">
      <div v-loading="profileLoad">
        <el-row class="avatar">
          <el-col>
            <el-upload
              class="avatar-uploader"
              :action="uploadUrl"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              :headers="{ 'Authorization': token }"
              :key="'avatar'"
            >
            <el-avatar :size="150" :src="this.baseUrl + this.profileForm.avatar" >
              <template  #default>
                <el-icon style="font-size: 80px; padding-top: 30px" ><UserFilled/></el-icon>
              </template>
            </el-avatar>
            </el-upload>
          </el-col>
        </el-row>
      <el-form :model="profileForm" :rules="profileRules" ref="profileForm" label-position="left">
        <el-form-item prop="id" label="编号" :label-width="60" >
          <el-input v-model="profileForm.id" autocomplete="off" disabled/>
        </el-form-item>
        <el-form-item prop="id" label="用户名" :label-width="60" >
          <el-input v-model="profileForm.username" autocomplete="off" disabled />
        </el-form-item>
        <el-form-item prop="email" label="邮箱" :label-width="60" >
          <el-input v-model="profileForm.email" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="phone" label="手机号" :label-width="60" >
          <el-input v-model="profileForm.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="sex" label="性别" :label-width="60" >
          <div class="my-4 flex items-center text-sm">
          <el-radio-group style="float: left; line-height: 40px" v-model="profileForm.sex" class="ml-4" >
              <el-radio :label=0 size="small">男</el-radio>
              <el-radio :label=1 size="small">女</el-radio>
          </el-radio-group>
          </div>
        </el-form-item>

      </el-form>
        <el-row class="info-time" justify="center">
          <el-col :span="8">
            <h3 >在职时间：&nbsp;&nbsp;{{this.profileForm.startDate}}</h3>
          </el-col>
          <el-col :span="2">
            <h3 >——</h3>
          </el-col>
          <el-col :span="8">
            <h3 style="text-align: left">&nbsp;&nbsp;{{ this.profileForm.endDate}}</h3>
          </el-col>
        </el-row>
      </div>
      <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="submitForm('profileForm', $event)">确定</el-button>
        <el-button @click="closeForm('profileForm', $event)">返回</el-button>

      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script >
// @ is an alias to /src
import { ref } from 'vue'
import { Setting, Message, Menu as IconMenu, ArrowDown,
  Avatar, Menu, User, Money, UserFilled } from '@element-plus/icons'
import { logoutAPI } from '@/api/login'
import storage from '@/utils/storage'
import { getProfile, editProfile } from '@/api/employee'

export default {
  name: 'HomeView',
  components: {
    Money,
    User,
    Menu,
    Avatar,
    Message,
    IconMenu,
    Setting,
    ArrowDown,
    UserFilled
  },
  data() {

    const validateEmail = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入邮箱'))
      } else {
        const reg = /^[A-Za-z\d_-]+@[a-zA-Z\d_-]+(\.[a-zA-Z\d_-]+)+$/
        if (!reg.test(this.profileForm.email)) {
          callback(new Error('格式错误'))
        }
      }
      callback()
    }
    const validatePhone = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入手机号'))
      } else {
        const reg = /^1(3\d|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8\d|9[0-35-9])\d{8}$/
        if (!reg.test(this.profileForm.phone)) {
          callback(new Error('格式错误'))
        }
      }
      callback()
    }
    return {
      baseUrl: "http://127.0.0.1:8088/api/",
      token: storage.get('token'),
      uploadUrl: 'http://127.0.0.1:8088/api/employee/uploadAvatar',
      openProfileDialog: false,
      profileLoad: false,
      nowTime: '',
      //
      profileForm: {
        id: 0,
        username: '',
        email: '',
        phone: '',
        sex: 0,
        avatar: '',
        startDate: '',
        endDate: '',
      },
      profileRules: {
        email: [
          { validator:validateEmail, require: true, trigger: 'blur' }
        ],
        phone: [
          { validator:validatePhone, require: true, trigger: 'blur' }
        ],
      }
    };
  },
  methods: {
    handleAvatarSuccess(res, file) {
      // 这句话就是连接后台服务器的
      if (res.success) {
        this.profileForm.avatar = res.data.data
        this.profileForm.avatar = URL.createObjectURL(file.raw)

        console.log("handleAvatarSuccess", this.profileForm.avatar)
      }else {
        this.$message.error("上传失败")
      }
    },
    beforeAvatarUpload(file) {
      console.log(this.token)
      const isJPG =file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG/PNG 格式!")
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!")
      }
      return (isJPG) && isLt2M
    },
    viewProfile() {
      this.profileLoad = true
      this.openProfileDialog = true
      getProfile().then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            //console.log(data)
            this.profileForm.id = data['id']
            this.profileForm.username = data['username']
            this.profileForm.email = data['email']
            this.profileForm.phone = data['phone']
            this.profileForm.sex = parseInt(data['sex'])
            this.profileForm.avatar = data['avatar']
            this.profileForm.startDate = data['startDate']
            this.profileForm.endDate = data['endDate']
            this.profileLoad = false
            console.log(this.baseUrl + this.profileForm.avatar)
          }else {
            this.$message.error(res.data.message);
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message);
        }
      )
    },
    closeForm(formName, event) {
      this.myBlur(event)
      this.openProfileDialog = false
    },
    submitForm (formName, event) {
      this.myBlur(event)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if ( formName === "profileForm" ) {
            this.profileLoad = true
            editProfile(this.profileForm).then(
              (res) => {
                if (res.data.success) {
                  this.profileLoad = false
                  this.$message.success("修改成功")
                }else {
                  this.$message.error(res.data.message);
                }
              }
            ).catch(
              (err) => {
                this.$message.error(err.message);
              }
            )
          }
        }else {
          console.log('error submit!!')
          return false
        }
      })
    },
    myBlur(event) {
      let tar = event.target
      while (tar.nodeName !== "BUTTON") {
        tar = tar.parentNode
      }
      tar.blur()
    },

    //显示当前时间（年月日时分秒）
    timeFormate (timeStamp) {
      let year = new Date(timeStamp).getFullYear();
      let month = new Date(timeStamp).getMonth() + 1 < 10 ? "0" + (new Date(timeStamp).getMonth() + 1) : new Date(timeStamp).getMonth() + 1;
      let date = new Date(timeStamp).getDate() < 10 ? "0" + new Date(timeStamp).getDate() : new Date(timeStamp).getDate();
      let hh = new Date(timeStamp).getHours() < 10 ? "0" + new Date(timeStamp).getHours() : new Date(timeStamp).getHours();
      let mm = new Date(timeStamp).getMinutes() < 10 ? "0" + new Date(timeStamp).getMinutes() : new Date(timeStamp).getMinutes();
      let ss = new Date(timeStamp).getSeconds() < 10 ? "0" + new Date(timeStamp).getSeconds() : new Date(timeStamp).getSeconds();
      this.nowTime = year + "年" + month + "月" + date + "日" + " " + hh + ":" + mm + ':' + ss;
    },
    nowTimes () {
      this.timeFormate(new Date());
      setInterval(this.nowTimes, 1000);
      this.clear()
    },
    clear () {
      clearInterval(this.nowTimes)
      this.nowTimes = null;
    },
    logout() {
      logoutAPI().
      then(
        (res) => {
          this.$store.commit('del_token')
          storage.remove("token")
          this.$router.replace("/login")
        }
      )
        .catch(
          (error) => {
            this.$message.error(error.message)
            // console.log(error)
            return false
          }
        )
    }
  },
  mounted () {
    this.nowTimes()
  }
}
</script>


<style >
.main-header {
  background-color: #24406f;
  height: 40px;
  position: fixed;
  z-index: 2;
  width: 100%;
}
.main-side-bar .el-aside {
  position: fixed;
  width: 14%;
  z-index: 1;
  margin-top: 40px;
}
.main-side-bar .el-header {
  position: relative;
  background-color: #130609;
}
.main-side-bar .el-aside {
  background: #223449;
}
.main-side-bar .el-menu {
  border-right: none;
  color: white;
}
.main-side-bar .el-main {
  padding: 0;
}
.main-side-bar .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
.main-side-bar .el-menu-item-group {
  background-color: #1e2232;
  color: #a1b0b9;
}
.main-side-bar .el-menu-item {
  color: white;
  background-color: #1e222d;
}
.main-side-bar .el-submenu {
  background-color: #223449;
  color: white;
}
.main-side-bar .el-submenu__title {
  color: white;
}

.main-side-bar .el-menu-item-group__title {
  color: #a1b0b9;
  margin-left: 0px;
  padding-left: 0px !important;
}
.profile-dropdown .el-dropdown{
  margin-top: 10px;
  color: white;
}
.profile-dropdown .el-icon {
  font-size: 20px;
}
.header-title {
  margin-top: 5px;
  float: left;
  color: white;
  height: 40px;
  font-size: 20px;
  text-align: justify;
}
.header-title .el-icon {
  font-size: 30px;
}
.main-side-bar .el-icon {
  font-size: 18px;
}
body {
  height: 600px;
}
.clock {
  display: flex;
  align-items: center;
  height: 33px;
  width: 300px;
  font-size: 18px;
  text-align: center;
}
.avatar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
