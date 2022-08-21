import axios from 'axios'
import router from '../router/router'
import {ElMessage} from "element-plus";
axios.defaults.timeout = 5000
axios.defaults.withCredentials = true

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8'
// axios.defaults.headers.post['Content-Type'] = 'multipart/form-data'

axios.defaults.baseURL = 'http://120.25.161.31'
axios.interceptors.response.use(
    response => {
        if (response.status === 200) {
            return Promise.resolve(response)
        } else {
            return Promise.reject(response)
        }
    },
    error => {
        if (error.response.status) {
            switch (error.response.status) {
                case 401:
                    localStorage.setItem("token","")
                    localStorage.setItem("consumer","")
                    ElMessage.error(error.response.data.errorMsg);
                    break
                case 403:
                    localStorage.setItem("token","")
                    localStorage.setItem("consumer","")
                    ElMessage.error(error.response.data.errorMsg);
                    break
                case 404:
                    break
            }
            return Promise.reject(error.response)
        }
    }
)

export function get (url, params = {}) {
    return new Promise((resolve, reject) => {
            axios.get(url,
                {params: params,
                headers:{
                    "Authorization":localStorage.getItem("token"),
                }})
                .then(response => {
                    resolve(response.data)
                })
                .catch(err => {
                    reject(err)
                })
        }
    )
}

export function post (url, data = {}) {
    return new Promise((resolve, reject) => {
            axios.post(url, data,{headers:{
                    "Authorization":localStorage.getItem("token")
                     },
            })
                .then(response => {
                    resolve(response.data)
                })
                .catch(err => {
                    reject(err)
                })
        }
    )
}