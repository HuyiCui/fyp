import axios from 'axios'
import router from "@/router";

// Create a new axios object
const request = axios.create({
    baseURL: process.env.VUE_APP_BASEURL,   // Backend interface address ip:port
    timeout: 30000                          // 30s request timeout
})

// Request Interceptor
// You can do some processing on the request before sending it
// For example, uniformly add tokens and uniformly encrypt request parameters
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';        // Set the request header format
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}')  // Get cached user information
    config.headers['token'] = user.token  // Set request headers

    return config
}, error => {
    console.error('request error: ' + error) // for debug
    return Promise.reject(error)
});

// Response Interceptor
// The results can be processed uniformly after the interface responds
request.interceptors.response.use(
    response => {
        let res = response.data;

        // Compatible with string data returned by the server
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401') {
            router.push('/login')
        }
        return res;
    },
    error => {
        console.error('response error: ' + error) // for debug
        return Promise.reject(error)
    }
)


export default request