<template>
  <div class="personal-salary">
    <el-container class="salary-box" >
      <el-header  class="sub-header">
        <el-row style="height: 100%">
          <el-col :span="4">
            <span style="float:left;
            line-height: 40px;
            margin:0;"><b>薪资查询</b></span>
          </el-col>
        </el-row>
      </el-header>
      <el-main class="main" v-loading="mainLoad">
        <el-row>
          <el-descriptions
            style="width: 100%;"
            title="当前薪资"
            :column="8"
            border
            direction="vertical"
            ref="currentSalary"
            id="currentSalary"
          >
            <template #extra>
              <el-button v-if="notPrint" type="primary" @click="this.savePdf('currentSalary', $event)">打印</el-button>
            </template>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  编号
                </div>
              </template>
                  {{this.employee.id}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  姓名
                </div>
              </template>
              {{this.employee.username}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  基础工资
                </div>
              </template>
              {{this.salary.basePay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  绩效奖金
                </div>
              </template>
              {{this.salary.meritPay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  补贴
                </div>
              </template>
              {{this.salary.subsidyPay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  加班费
                </div>
              </template>
              {{this.salary.overtimePay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  保险
                </div>
              </template>
              {{this.salary.insurancePay}}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <div class="cell-item">
                  实发工资
                </div>
              </template>
              {{this.salary.basePay + this.salary.insurancePay + this.salary.meritPay
                + this.salary.overtimePay + this.salary.subsidyPay }}
            </el-descriptions-item>
          </el-descriptions>

        </el-row>
        <el-button style="float:right; margin-top: 10px" type="primary" @click="viewPie($event)">查看分布</el-button>
        <el-row>
          <el-col>
            <h3 style="float: left;">薪资发放历史查询</h3>
          </el-col>
        </el-row>
        <el-row class="time-block row-bg" >
          <el-col :span="2">
            <span style="line-height: 40px; float: right; ">发放日期:</span>
          </el-col>
          <el-col :span="6" style="margin-left: 2%;">
            <div class="start-block">
              <el-date-picker
                v-model="startDate"
                type="date"
                placeholder="Pick a day"
                @change="this.requestData"
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
                @change="this.requestData"
              />
            </div>
          </el-col>
          <el-col :span="3">
            <el-button type="primary" @click="clearDate">
              清空
            </el-button>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" @click="savePdf('history', $event)">
              打印
            </el-button>
          </el-col>
          <el-col :span="3" >
            <el-button style="margin-left: 20px" type="success" @click="viewLine($event)" >查看走势</el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-table
            class="main-table"
            :data="salaryTable"
            style="margin-bottom: 20px; margin-top: 2%"
            row-key="id"
            stripe
            border
            default-expand-all
            max-height="500px"
            height="500px"
            ref="history"
            id="history"
          >
            <el-table-column prop="id" align="center" label="编号"  />
            <el-table-column prop="basePay" align="center" label="基础工资"  />
            <el-table-column prop="meritPay" align="center" label="绩效奖金"  />
            <el-table-column prop="subsidyPay" align="center" label="补贴"  />
            <el-table-column prop="overtimePay" align="center" label="加班费"  />
            <el-table-column prop="insurancePay" align="center" label="保险"  />
            <el-table-column prop="sumPay" align="center" label="实发工资"  />
            <el-table-column prop="payTime" align="center" label="发放时间" width="100px" />
          </el-table>
        </el-row>
      </el-main>
    </el-container>
    <el-dialog v-model="openPie" :width="800" title="薪资分布">
      <div id="pie" style="width: 800px;height:400px;"></div>
    </el-dialog>
    <el-dialog v-model="openLine" :width="850" title="薪资走势">
      <div id="line" style="width: 800px;height:600px;"></div>
    </el-dialog>
  </div>
</template>

<script>
import { getPersonalSalaryAPI, getSalaryRecordAPI } from '@/api/salary'
import { getEmployeeInfoAPI } from '@/api/employee'
import htmlToPDF from '@/utils/htmlToPDF'
import * as echarts from 'echarts'

export default {
  name: 'PersonalSalaryView',
  components: {

  },
  data () {
    return {
      openLine: false,
      openPie: false,
      notPrint: true,
      mainLoad: false,
      startDate: null,
      endDate: null,
      salary: {
        id: 0,
        employeeId: 0,
        basePay: 0,
        meritPay: 0,
        insurancePay: 0,
        subsidyPay: 0,
        overtimePay: 0,
      },
      employee: {
        id: 0,
        name: "NULL",
      },
      salaryTable: [],
    }
  },
  methods: {
    viewLine(event) {
      this.myBlur(event)
      this.openLine = true
      this.$nextTick(() => {
        var lineChart = echarts.init(document.getElementById('line'))
        var option = {
          xAxis: {
            data: this.salaryTable.map(
              item => {
                return item.payTime.substring(0, item.payTime.indexOf(" "))
              }
            ).reverse()
          },
          legend: {
            orient: 'vertical',
            x: 'right',
            data: ['基础工资', '绩效奖金', '补贴', '加班费', '保险', '实发工资']
          },
          tooltip: {
            trigger: 'item'
          },
          yAxis: {},
          series: [
            {
              data: this.salaryTable.map(item => {return item.basePay}).reverse(),
              type: 'line',
              name: "基础工资"
            },
            {
              data: this.salaryTable.map(item => {return item.meritPay}).reverse(),
              type: 'line',
              name: "绩效奖金"
            },
            {
              data: this.salaryTable.map(item => {return item.subsidyPay}).reverse(),
              type: 'line',
              name: "补贴"
            },
            {
              data: this.salaryTable.map(item => {return item.overtimePay}).reverse(),
              type: 'line',
              name: "加班费"
            },
            {
              data: this.salaryTable.map(item => {return item.insurancePay}).reverse(),
              type: 'line',
              name: "保险",
            },
            {
              data: this.salaryTable.map(item => {return item.sumPay}).reverse(),
              type: 'line',
              name: "实发工资",
              label: {
                show: true,
                position: 'top',
                textStyle: {
                  fontSize: 15
                }
              }
            }
          ]
        }
        //console.log(option.xAxis.data)
        lineChart.setOption(option)
      })
    },
    viewPie(event) {
      this.myBlur(event)
      this.openPie = true
      this.$nextTick(() => {
        var pieChart = echarts.init(document.getElementById('pie'))
        var option = {
          legend: {
            orient: 'vertical',
            x: 'left',
            data: ['基础工资', '绩效奖金', '补贴', '加班费', '保险']
          },
          tooltip: {
            trigger: 'item'
          },
          series: [
            {
              type: 'pie',
              radius: ['35%', '95%'],
              label: {
                show: false,
                fontSize:18,
                position: 'center',
                formatter: '{b}\n{d}%',
                emphasis: {
                  show: true
                }
              },
              avoidLabelOverlap: false,
              stillShowZeroSum: false,
              data: [
                {
                  value: this.salary.basePay === 0 ? null : this.salary.basePay,
                  name: "基础工资"
                },
                {
                  value: this.salary.meritPay === 0 ? null : this.salary.meritPay,
                  name: "绩效奖金"
                },
                {
                  value: this.salary.subsidyPay === 0 ? null : this.salary.subsidyPay,
                  name: "补贴"
                },
                {
                  value: this.salary.overtimePay === 0 ? null : this.salary.overtimePay,
                  name: "加班费"
                },
                {
                  value: -this.salary.insurancePay === 0 ? null : -this.salary.insurancePay,
                  name: "保险"
                },
              ]
            }
          ]
        }
        pieChart.setOption(option)
      })
    },
    myBlur(event) {
      let tar = event.target
      while (tar.nodeName !== "BUTTON") {
        tar = tar.parentNode
      }
      tar.blur()
    },
    clearDate() {
      this.startDate = null
      this.endDate = null
      this.requestData()
    },
    savePdf(refName, event){
      this.myBlur(event)
      if (refName === "currentSalary")
        this.notPrint = false
      //var self = this
      var t
      clearTimeout(t)
      t = setTimeout(function() {
        htmlToPDF.downloadPDF(document.querySelector('#' + refName), 'yourSalary')
        // window.eventHub.$emit('refreshList', '')// 更新右侧列表状态
        if (refName === "currentSalary")
          this.notPrint = true
      }, 500)
    },
    requestData() {
      const pack = {
        startDate: this.startDate,
        endDare: this.endDate
      }
      getSalaryRecordAPI(pack).then(
        (res) => {
          if(res.data.success) {
            this.salaryTable = res.data.data
            this.mainLoad = false
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
  },
  mounted () {
    this.mainLoad = true
    getPersonalSalaryAPI().then(
      (res) => {
        if (res.data.success) {
          this.salary = res.data.data
          //console.log(this.salary)
          if (this.salary === null) return
          getEmployeeInfoAPI(this.salary.employeeId).then(
            (res) => {
              if (res.data.success) {
                this.employee = res.data.data
                //console.log(this.employee.id)
              }else {
                this.$message.error(res.data.message)
              }
            }
          )
        }else {
          this.$message.error(res.data.message)
        }
      }
    ).catch(
      (err) => {
        this.$message.error(err.message)
      }
    )
    this.requestData()
  }
}
</script>

<style scoped>
.personal-salary {
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
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.salary-box .main{
  width: 90%;
  margin-left: 5%;
  margin-top: 1%;
  overflow-y: hidden;
}
</style>
