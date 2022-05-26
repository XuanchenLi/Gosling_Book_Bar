<template>
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
                <el-dropdown-item>修改个人信息</el-dropdown-item>
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
          <el-menu :default-openeds="['1']">
            <el-submenu index="1">
              <template #title>
                <el-icon><user /></el-icon>&ensp;员工管理
              </template>
              <el-menu-item index="1-1">员工查询</el-menu-item>
              <el-menu-item index="1-2">部门管理</el-menu-item>
              <el-menu-item index="1-3">岗位调度</el-menu-item>
            </el-submenu>
            <el-submenu index="2">
              <template #title>
                <el-icon><money /></el-icon>&ensp;财务系统
              </template>
              <el-menu-item-group>
                <template #title>财务统计</template>
                <el-menu-item index="2-1">盈利统计</el-menu-item>
                <el-menu-item index="2-2">工资统计</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="员工工资">
                <el-menu-item index="2-3">工资查询</el-menu-item>
              </el-menu-item-group>
            </el-submenu>
            <el-submenu index="3">
              <template #title>
                <el-icon><setting /></el-icon>&ensp;系统管理
              </template>
              <el-menu-item index="3-1">账号控制</el-menu-item>
              <el-menu-item index="3-2">权限控制</el-menu-item>
            </el-submenu>
          </el-menu>
        </el-scrollbar>
        <div style="height: 60px; background-color: #24406f; margin-top: 0">
        </div>
      </el-aside>
      <router-link to="/home/searchEmployee"></router-link>
      <router-view></router-view>
    </el-container>
  </div>
</template>

<script >
// @ is an alias to /src
import { ref } from 'vue'
import { Setting, Message, Menu as IconMenu, ArrowDown, Avatar, Menu, User, Money } from '@element-plus/icons'
import { logoutAPI } from '@/api/login'
import storage from '@/utils/storage'

const item = {
  date: '2016-05-02',
  name: 'Tom',
  address: 'No. 189, Grove St, Los Angeles',
}
const tableData = ref(Array.from({ length: 20 }).fill(item))

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
    ArrowDown
  },
  data() {
    return {
      nowTime: ''
    };
  },
  methods: {
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
</style>
