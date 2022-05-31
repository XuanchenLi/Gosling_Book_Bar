<template>
  <div class="search-employee">
    <el-container class="department-box">
      <el-header  class="sub-header">
        <el-row style="height: 100%">
          <el-col :span="4">
            <span style="float:left;
            line-height: 40px;
            margin:0;"><b>员工管理</b></span>
          </el-col>
        </el-row>
      </el-header>
      <el-main class="search-box">
        <el-row class="searcher" justify="space-evenly">
          <el-col :span="5">
            <div style="float: right">
              <el-select v-model="typeSelect" placeholder="Select" style="width: 110px">
                <el-option label="全部员工" value="0" />
                <el-option label="在职员工" value="1" />
                <el-option label="离职员工" value="2" />
              </el-select>
            </div>
          </el-col>
          <el-col :span="13">
            <el-autocomplete
              v-model="searchInput"
              :fetch-suggestions="getRecommend"
              clearable
              @select="handleSelect"
              @keyup.enter.native="requestData"
              style="width: 505px; margin-left: 6%"
              :trigger-on-focus="false"
            >
              <template #prepend>
                <el-select v-model="regionSelect" placeholder="Select" style="width: 100px">
                  <el-option label="账户名" value="0" />
                  <el-option label="编号" value="1" />
                </el-select>
              </template>
              <template #append>
                <el-button @click="requestData" style="width: 50px; padding: 10px">
                  <el-icon><Search/></el-icon>
                </el-button>
              </template>
            </el-autocomplete>
          </el-col>
        </el-row>

        <el-row class="time-block row-bg" justify="space-evenly">
          <el-col :span="5">
            <span style="line-height: 40px; float: right; ">入职时间:</span>
          </el-col>
          <el-col :span="6" style="margin-left: 2%">
            <div class="start-block">
              <el-date-picker
                v-model="startDate"
                type="date"
                placeholder="Pick a day"
                :shortcuts="shortcuts"
              />
            </div>
          </el-col>
          <el-col :span="1">
            <span style="line-height: 40px;">——</span>
          </el-col>
          <el-col :span="6">
            <div class="end-block">
              <el-date-picker
                v-model="endDate"
                type="date"
                placeholder="Pick a day"
                :shortcuts="shortcuts"
              />
            </div>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" @click="clearDate">
              清空
            </el-button>
          </el-col>
        </el-row>
      </el-main>
      <el-main class="main" ref="mainWindow" v-loading="this.mainLoading">
        <el-table
          class="main-table"
          :data="tableData"
          style="margin-bottom: 20px"
          row-key="id"
          stripe
          border
          default-expand-all
        >
          <el-table-column prop="id" align="center" label="编号" sortable width="70" />
          <el-table-column prop="name" align="center" label="姓名" sortable />
          <el-table-column prop="sex" align="center" label="性别" sortable />
          <el-table-column prop="startDate" align="center" label="入职日期" sortable />
          <el-table-column prop="endDate" align="center" label="离职日期" sortable />
          <el-table-column align="center" label="操作" width="200" >
            <template #default="scope">
              <el-button size="small" type="warning" :disabled="this.tableData[scope.$index].endDate !== null"
                         @click="allocate(scope.$index, $event)">岗位调度</el-button>
              <el-button size="small" type="primary" @click="viewInfo(scope.$index, $event)">查看信息</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination-block">
          <el-pagination
            v-model:currentPage="currentPage"
            v-model:page-size="pageSize"
            :background=true
            layout="prev, pager, next, jumper"
            :total="totalSize"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-main>
    </el-container>
    <!--  -->
    <el-dialog v-model="openAllocateDialog" title="岗位调度">
      <el-row style="margin-bottom: 20px">
        <el-col  :span="12" class="info" >
          <div>
            <el-row>
              <h2 style="float:left;">账户名：{{this.username}}</h2>
            </el-row>
            <el-row>
              <h3 >编号：{{this.id}}</h3>
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-card v-loading="allocateLoad" class="position-card">
        <template #header>
          <el-row class="card-header">
            <el-col :span="3">
              <span>所在岗位</span>
            </el-col>
            <el-col :span="7">
              <el-select v-model="department" @change="fillPosition"  placeholder="Select">
                <el-option
                  v-for="item in departments"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-col>
            <el-col :span="7" style="margin-left: 10px">
              <el-select v-model="position"  placeholder="Select" >
                <el-option
                  v-for="item in positions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                  :disabled="item.disabled"
                />
              </el-select>
            </el-col>
            <el-col :span="4">
              <el-button class="button" size="small"
                         type="success" text
                         @click="addPosition"
              >
                添加岗位</el-button>
            </el-col>
          </el-row>
        </template>
        <el-table :data="departmentPositionList"
                  height="250"
                  stripe
                  max-height="400px"
                  style="width: 100%">
          <el-table-column prop="departmentName" align="center" label="部门" />
          <el-table-column prop="positionName" align="center" label="岗位"  />
          <el-table-column align="center" label="" width="100" >
            <template #default="scope">
              <el-button style="width: 32px; height: 32px;padding: 0"
                         size="small" type="danger"
                         @click="deleteEmployeePosition(scope.$index, $event)"
              >
                <el-icon><CloseBold/></el-icon>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

    </el-dialog>
    <!--  -->
    <el-dialog
               v-model="openInfoDialog"
               title="查看员工信息"
    >
      <div v-loading="this.infoLoad">
      <el-row>
        <el-col class="avatar" :span="8" :offset="2">
          <el-image>
            <template #error>
              <div class="image-slot">
                <el-icon><Avatar /></el-icon>
              </div>
            </template>
          </el-image>
        </el-col>
        <el-col  :span="12" class="info" :offset="2">
          <div>
            <el-row>
              <h2 style="float:left;">账户名：{{this.username}}</h2>
            </el-row>
            <el-row>
              <h3 >编号：{{this.id}}</h3>
            </el-row>
            <el-row>
              <h3 >邮箱：{{this.email}}</h3>
            </el-row>
            <el-row>
              <h3 >手机号：{{this.phone}}</h3>
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-row class="info-time" justify="center">
        <el-col :span="8">
          <h3 >在职时间：&nbsp;&nbsp;{{this.mStartDate}}</h3>
        </el-col>
        <el-col :span="2">
          <h3 >——</h3>
        </el-col>
        <el-col :span="8">
          <h3 style="text-align: left">&nbsp;&nbsp;{{ this.mEndDate }}</h3>
        </el-col>
      </el-row>

      <el-row class="job-row">
        <el-descriptions
          class="margin-top"
          title="所在部门岗位"
          :column="2"
          :size="10"
          border
          align="center"
        >
          <div v-if="departmentPositionList.length === 0">
            <el-descriptions-item >
              <template #label>
                <div class="cell-item">
                  <el-icon >
                    <OfficeBuilding />
                  </el-icon>
                  部门
                </div>
              </template>
              暂无数据
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  <el-icon >
                    <Suitcase />
                  </el-icon>
                  岗位
                </div>
              </template>
              暂无数据
            </el-descriptions-item>
          </div>
          <div v-for="(value, index) in departmentPositionList" :key="index">
            <el-descriptions-item >
              <template #label>
                <div class="cell-item">
                  <el-icon >
                    <OfficeBuilding />
                  </el-icon>
                  部门
                </div>
              </template>
              {{value.departmentName}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  <el-icon >
                    <Suitcase />
                  </el-icon>
                  岗位
                </div>
              </template>
              {{value.positionName}}
            </el-descriptions-item>
          </div>
        </el-descriptions>
      </el-row>
      </div>
      <el-row>
        <el-col :span="24">
          <h2 style="float:left;"> 岗位调度历史 </h2>
        </el-col>
      </el-row>
      <el-row style="width: 100%;">
        <el-col :span="24">
          <hr style="color:#42b983"/>
        </el-col>
      </el-row>
      <el-row>

        <el-col :span="24">
          <el-scrollbar height="200px">
            <el-timeline  >
              <el-timeline-item
                v-for="(activity, index) in history"
                :key="index"
                :icon="activity.icon"
                :type="activity.type"
                :color="activity.color"
                :size="activity.size"
                :timestamp="activity.timestamp"
                style="text-align: left;"
              >
                <div style="float: left; width: 100%; text-align: left">{{ activity.content }}</div>
              </el-timeline-item>
            </el-timeline>
          </el-scrollbar>
        </el-col>
      </el-row>

    </el-dialog>
  </div>
</template>

<script>
import HelloWorld from '@/components/HelloWorld'

import { Search } from '@element-plus/icons'
import { requestEmployeeDateAPI, getEmployeeRecommend } from '@/api/search'
import { Picture, Avatar, MoreFilled, OfficeBuilding, Suitcase, CloseBold } from '@element-plus/icons'
import { getEmployeeInfoAPI, getEmployeePositionAPI,
  deleteEmployeePositionAPI, addEmployeePositionAPI, getEmployeeAllocateHistory } from '@/api/employee'
import { getAllBasicAPI } from '@/api/department'
import { getAllBasicByDepartmentIdAPI } from '@/api/position'

export default {
  name: 'SearchEmployeeView',
  components: {
    Search,
    Picture,
    Avatar,
    MoreFilled,
    OfficeBuilding,
    Suitcase,
    CloseBold
  },
  data () {
    return {
      openAllocateDialog: false,
      allocateLoad: false,
      openInfoDialog: false,
      infoLoad: false,
      currentPage: 1,
      pageSize: 10,
      totalSize: 1,
      searchInput: "",
      mainLoading: false,
      typeSelect: "0",
      regionSelect: "0",
      tableData: [],
      startDate: null,
      endDate: null,
      shortcuts: [
        {
          text: 'Today',
          value: new Date(),
        },
        {
          text: 'Yesterday',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            return date
          },
        },
        {
          text: 'A week ago',
          value: () => {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            return date
          },
        },
      ],
      //个人信息
      username: "",
      sex: "",
      id: 0,
      mStartDate: null,
      mEndDate: null,
      phone: "",
      email: "",
      history: [],
      departmentPositionList: [],
      //调度信息
      department: null,
      position: null,
      departments: [],
      positions: [],
    }
  },
  methods: {
    deleteEmployeePosition(index, event) {
      this.myBlur(event)
      let position = this.departmentPositionList[index]
      deleteEmployeePositionAPI(this.id, position.positionId).then(
        (res) => {
          if (res.data.success) {
            this.departmentPositionList.splice(index, 1)
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    }
    ,
    clearInfo() {
      this.username = ""
      this.sex = ""
      this.id = 0
      this.mStartDate = null
      this.mEndDate = null
      this.phone = ""
      this.email = ""
      this.departmentPositionList = []
      this.history = []
    },
    setInfo(profile) {
      this.username = profile.username
      this.sex = profile.sex === 0 ? "男" : "女"
      this.id = profile.id
      this.mStartDate = profile.startDate
      this.mEndDate = profile.endDate
      this.phone = profile.phone
      this.email = profile.email
    }
    ,
    myBlur(event) {
      let tar = event.target
      while (tar.nodeName !== "BUTTON") {
        tar = tar.parentNode
      }
      tar.blur()
    },
    allocate(index, event) {
      this.myBlur(event)
      this.department = null
      this.position = null
      this.openAllocateDialog = true
      this.allocateLoad = true
      this.clearInfo()
      let data = this.tableData[index]
      this.id = data.id
      this.username = data.name
      getEmployeePositionAPI(data.id).then(
        (res) => {
          if (res.data.success) {
            this.departmentPositionList = res.data.data
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
      getAllBasicAPI().then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            this.departments = []
            for (let index in data) {
              this.departments.push(
                {
                  value: data[index].id,
                  label: data[index].name
                }
              )
            }
            this.allocateLoad = false
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      ).finally(
      )
    },
    isExist(id) {
      for (let index in this.departmentPositionList) {
        if (id === this.departmentPositionList[index].positionId)
          return true
      }
      return false
    }
    ,
    fillPosition() {
      this.positions = []
      this.position = null
      getAllBasicByDepartmentIdAPI(this.department).then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            this.positions = []
            for (let index in data) {
              this.positions.push(
                {
                  value: data[index].id,
                  label: data[index].name,
                  disabled: this.isExist(data[index].id)
                }
              )
            }
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    getDepartmentName(id) {
      for (let index in this.departments) {
        if (this.departments[index].value === id)
          return this.departments[index].label
      }
      return ""
    },
    getPositionName(id) {
      for (let index in this.positions) {
        if (this.positions[index].value === id)
          return this.positions[index].label
      }
      return ""
    },
    addPosition (event) {
      this.myBlur(event)
      addEmployeePositionAPI(this.id, this.position).then(
        (res) => {
          if (res.data.success) {
            this.departmentPositionList.push(
              {
                positionId: this.position,
                departmentId: this.department,
                positionName: this.getDepartmentName(this.department),
                departmentName: this.getPositionName(this.position),
              }
            )
            this.position = null
            this.department =null
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
    },
    viewInfo(index, event) {
      this.myBlur(event)
      this.openInfoDialog = true
      this.infoLoad = true
      this.clearInfo()
      let data = this.tableData[index]
      getEmployeeInfoAPI(data.id).then(
        (res) => {
          if (res.data.success) {
            let profile = res.data.data
            this.setInfo(profile)
            // TODO 加载头像
            console.log(profile)
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
      getEmployeePositionAPI(data.id).then(
        (res) => {
          if (res.data.success) {
            this.departmentPositionList = res.data.data
          }else {
            this.$message.error(res.data.message)
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
        }
      )
      getEmployeeAllocateHistory(data.id).then(
        (res) => {
          if (res.data.success) {
            console.log(res.data.data)
            let data = res.data.data
            this.history = []
            for (let index in data) {
              this.history.push(
                {
                  timestamp: data[index].allocateDate,
                  content: (data[index].type === 0 ? "+ " : "- ") +
                    data[index].departmentName + " - " + data[index].positionName,
                  color: (data[index].type === 0 ? "#2E769F" : "#9B312E")
                }
              )
            }
            this.infoLoad = false
          }else {
            this.$message.error(res.data.message)
          }
        }
      )
        .catch(
          (err) => {
            this.$message.error(err.message)
          }
        )
    }
    ,
    requestData() {
      const searchBag = {
        currentPage: this.currentPage === "undefined" ? 1 : this.currentPage,
        regionSelect: parseInt(this.regionSelect),
        typeSelect: parseInt(this.typeSelect),
        startDate: this.startDate,
        endDate: this.endDate,
        query: this.searchInput
      }
      // console.log(searchBag)
      this.mainLoading = true
      requestEmployeeDateAPI(searchBag).then(
        (res) => {
          if (res.data.success) {
            let data = res.data.data
            this.currentPage = data.currentPage
            this.totalSize = data.totalNum
            this.mainLoading = false
            this.tableData = []
            for (let i = 0; i < data.employeeList.length; ++i){
              this.tableData.push(
                {
                  id: data.employeeList[i].id,
                  name: data.employeeList[i].username,
                  startDate: data.employeeList[i].startDate,
                  endDate: data.employeeList[i].endDate,
                  sex: data.employeeList[i].sex === 0 ? "男" : "女",
                }
              )
            }
            // console.log(res.data.data)
          }else {
            this.$message.error(res.data.message)
            this.mainLoading = false
          }
        }
      ).catch(
        (err) => {
          this.$message.error(err.message)
          this.mainLoading = false
        }
      )
    },
    clearDate() {
      this.startDate = null
      this.endDate = null
    },
    handleSizeChange(val) {
      console.log(`${val} items per page`)
    },
    handleCurrentChange(val) {
      this.requestData()
    },
    getRecommend(queryString, cb){
      let results = []
      if (this.regionSelect === 1 || this.searchInput === "" || this.searchInput === null)
        cb(results)
      const searchBag = {
        currentPage: this.currentPage === "undefined" ? 1 : this.currentPage,
        regionSelect: parseInt(this.regionSelect),
        typeSelect: parseInt(this.typeSelect),
        startDate: this.startDate,
        endDate: this.endDate,
        query: this.searchInput
      }
      getEmployeeRecommend(searchBag).then(
        (res) => {
          var timeout
          if (res.data.success) {
            let data = res.data.data
            for (let i = 0; i < data.length; ++i) {
              results.push({ value: data[i], address: ""})
            }
            console.log(results)
            results = queryString
              ? results.filter(this.createFilter(queryString))
              : results;
            clearTimeout(timeout)
            timeout = setTimeout(
              () => {
                cb(results)
              }, 2000)
            // console.log(res.data.data)
            cb(results)
          } else {
            cb(results)
          }
        }
      ).catch(
        (err) => {
          cb(results)
        }
      )
      // call callback function to return suggestions
      cb(results)
    }
    ,
    handleSelect(item) {
      this.requestData()
    },
    createFilter(queryString) {
      return (item) => {
        return item.value.toUpperCase().match(queryString.toUpperCase());
      };
    },
  },
  created () {
  }
  ,
  mounted () {
    this.requestData()
  }
}
</script>

<style scoped>
.search-employee {
  background-color: #f3f4f7;
  width: 86%;
  margin-left: 14%;
  height: auto;
  float: right;
  margin-top: 40px;
  margin-right: 0;
  padding-right: 0;
}
.sub-header {
  background-color: white;
  height: 40px;
}
.search-box {
  width: 90%;
  margin-left: 5%;
  margin-top: 1%;
  overflow: hidden;
}

.main {
  margin-top: 1%;
}
.main-table {
  width: 90%;
  margin-left: 5%;
  height: 640px;
}
.pagination-block + .pagination-block {
  margin-top: 10px;
}
.time-block {
}

.search-box .searcher {

}
.time-block {
  margin-top: 1%;
}
.avatar .el-image {
  padding: 0 5px;
  width: 150px;
  height: 150px;
  border: #a1b0b9 solid 1px;
}
.avatar .image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  color: var(--el-text-color-secondary);
  font-size: 30px;
}
.avatar .image-slot .el-icon {
  font-size: 30px;
}

.info .el-row{
  height: 40px;
}
.info h2 {
  margin-top: 5px;
  margin-left: 0;
}
.info h3 {
  margin-top: 5px;
  margin-left: 0;
}
hr { display: block; height: 1px;

  border: 0; border-top: 1px solid #ccc;

  margin: 1em 0; padding: 0; }

.el-descriptions {
  margin-top: 20px;
}
.cell-item {
  display: flex;
  align-items: center;
}
.position-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.position-card .text {
  font-size: 14px;
}

.position-card .item {
  margin-bottom: 18px;
}


</style>
