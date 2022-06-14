import http from '@/utils/http'
let request = "/testIp/"

export function getAllBasicAPI() {
  // console.log(`${request}/department/all_basic`)
  return http.get(`${request}/department/all_basic`)
}

export function deleteDepartmentAPI(params) {
  return http.get(`${request}/department/delete_${params}`)
}

export function getDepartmentInfoAPI(params) {
  return http.get(`${request}/department/get_info_${params}`)
}

export function updateDepartmentAPI(id, data) {
  return http.post(`${request}/department/update_${id}`, data)
}

export function saveOrUpdateDepartmentAPI(data) {
  return http.post(`${request}/department/save_or_update`, data)
}

export function getDepartmentStatsAPI() {
  return http.get(`${request}/department/get_num`)
}
