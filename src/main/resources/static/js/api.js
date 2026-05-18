
// 基础配置
const baseUrl = "http://localhost:8080";

// axios 实例
const service = axios.create({
    baseURL: baseUrl,
    timeout: 5000,
    headers: {
        "Content-Type": "application/json;charset=utf-8"
    }
});

// ===================== 请求拦截器：自动带token（白名单放行）=====================
// 规则：登录/注册/退出接口 不带token，其他接口自动携带
service.interceptors.request.use(
    config => {
        // 白名单：不需要token的接口（和你后端接口路径完全一致）
        const whiteList = ["/admin/login", "/admin/register", "/admin/logout"];

        // 白名单接口：直接放行，不带token
        if (whiteList.includes(config.url)) {
            return config;
        }

        // 非白名单接口：自动从本地拿token，放进请求头
        const token = localStorage.getItem("token");
        if (token) {
            config.headers["token"] = token;
        }
        return config;
    },
    error => Promise.reject(error)
);

// ===================== 响应拦截器：处理未登录/过期自动跳转 =====================
service.interceptors.response.use(
    response => {
        const res = response.data;
        console.log("接口返回：", res); // 调试用，可保留

        // ========== 核心判断：未登录 / 登录已过期（完全匹配你的后端枚举） ==========
        // 你后端返回规则：未登录 → code="Forbidden"，msg="请先登录"
        // 登录过期 → code="Forbidden"，msg="登录已过期"
        if (res.code === "Forbidden" && (res.msg === "请先登录" || res.msg === "登录已过期")) {
            // 1. 清除本地登录信息（token + 其他用户数据）
            localStorage.clear(); // 或精准删除：localStorage.removeItem("token")
            // 2. 弹窗提示（和后端msg一致）
            console.log("是否过期",res.code);

            //window.Vue.$message.error("未登录或登录超时");
            const div = document.createElement('div');
            div.style.cssText = `position:fixed;top:20px;left:50%;transform:translateX(-50%);
      background:#fef0f0;color:#f56c6c;padding:10px 16px;
      border-radius:4px;z-index:9999;box-shadow:0 2px 12px rgba(0,0,0,0.1);`;
            div.textContent = "未登录或登录超时";
            document.body.appendChild(div);
            setTimeout(() => div.remove(), 2000);
            // 3. 延迟跳转到登录页（让用户看到提示）
            console.log("是否过期",res.msg);
            setTimeout(() => {
                window.location.href = "/login.html"; // 你的登录页地址
            }, 1000);
            console.log("是否过期",res.msg);
            // 4. 拒绝Promise，避免前端继续执行后续逻辑
            return Promise.reject(res);
        }

        // 正常响应：直接返回数据给前端
        return res;
    },
    error => {
        console.error("请求异常：", error);
        // 你后端不用HTTP状态码，这里只做基础网络错误提示
        ElementUI.Message.error("网络异常或服务错误，请稍后重试");
        return Promise.reject(error);
    }
);

// ===================== 接口定义（完全不变，和你原来一致）=====================
const api = {
    user: {
        list: (params) => service.get("/api/user/page", {params}),
        get: (id) => service.get(`/api/user?id=${id}`),
        add: (data) => service.post("/api/user", data),
        update: (data) => service.put("/api/user", data),
        del: (id) => service.delete(`/api/user?id=${id}`),
        batchDel: (ids) => service.delete("/api/user/batch", {data: ids}),
        login: (data) => service.post("/admin/login", data),
        logout: () => service.post("/admin/logout"),
        register: (data) => service.post("/admin/register", data)
    },
    test: {
        hello: () => service.get("/api/hello")
    }
};