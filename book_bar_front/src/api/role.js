import http from '@/utils/http'
let request = "/testIp/"

export function getAllRoleAPI() {
  return http.get(`${request}/role/get_all`)
}

export function getEmployeeRoleAPI(params) {
  return http.get(`${request}/employee/role/${params}`)
}

export function removeEmployeeRoleAPI(eId, rId) {
  return http.get(`${request}/employee/role/delete/${eId}_${rId}`)
}

export function addEmployeeRoleAPI(eId, rId) {
  return http.get(`${request}/employee/role/add/${eId}_${rId}`)
}

export function saveOrUpdateRoleAPI(data) {
  return http.post(`${request}/role/save_or_update`, data)
}

export function removeRoleAPI(params) {
  return http.get(`${request}/role/delete/${params}`)
}

export function getRoleMenuAPI(params) {
  return http.get(`${request}/role/get_menu/${params}`)
}
export function getRestRoleMenuAPI(params) {
  return http.get(`${request}/role/get_rest_menu/${params}`)
}

export function addRomeMenuAPI(rId, mId) {
  return http.get(`${request}/role/add_menu/${rId}_${mId}`)
}

export function removeRomeMenuAPI(rId, mId) {
  return http.get(`${request}/role/delete_menu/${rId}_${mId}`)
}
