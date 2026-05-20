const API_BASE = '';

const request = {
    get(url, params) {
        let query = '';
        if (params) {
            query = '?' + Object.keys(params).filter(k => params[k] !== undefined && params[k] !== null && params[k] !== '').map(k => `${k}=${encodeURIComponent(params[k])}`).join('&');
        }
        return fetch(API_BASE + url + query, {
            headers: this._headers(),
        }).then(this._handle);
    },
    post(url, data) {
        return fetch(API_BASE + url, {
            method: 'POST',
            headers: this._headers(),
            body: JSON.stringify(data),
        }).then(this._handle);
    },
    put(url, data) {
        return fetch(API_BASE + url, {
            method: 'PUT',
            headers: this._headers(),
            body: JSON.stringify(data),
        }).then(this._handle);
    },
    delete(url, params) {
        let query = '';
        if (params) {
            query = '?' + Object.keys(params).filter(k => params[k] !== undefined && params[k] !== null && params[k] !== '').map(k => `${k}=${encodeURIComponent(params[k])}`).join('&');
        }
        return fetch(API_BASE + url + query, {
            method: 'DELETE',
            headers: this._headers(),
        }).then(this._handle);
    },
    _headers() {
        const h = { 'Content-Type': 'application/json' };
        const token = Auth.getToken();
        if (token) h['Authorization'] = 'Bearer ' + token;
        return h;
    },
    _handle(res) {
        return res.json().then(data => {
            // code可能是枚举字符串"Success"或对象{code:200}
            const codeValue = typeof data.code === 'string' ? data.code : (data.code && data.code.code);
            if (codeValue && codeValue !== 200 && codeValue !== 'Success') {
                if (codeValue === 401 || codeValue === 'Unauthorized') {
                    Auth.logout();
                    window.location.href = '/login.html';
                    return;
                }
                throw new Error(data.msg || '请求失败');
            }
            return data;
        });
    }
};

const Auth = {
    getToken() {
        return localStorage.getItem('admin_token');
    },
    setToken(token) {
        localStorage.setItem('admin_token', token);
    },
    getUser() {
        const u = localStorage.getItem('admin_user');
        return u ? JSON.parse(u) : null;
    },
    setUser(user) {
        localStorage.setItem('admin_user', JSON.stringify(user));
    },
    isLoggedIn() {
        return !!this.getToken();
    },
    logout() {
        localStorage.removeItem('admin_token');
        localStorage.removeItem('admin_user');
    },
    checkLogin() {
        if (!this.isLoggedIn()) {
            window.location.href = '/login.html';
            return false;
        }
        return true;
    }
};

function formatDate(dateStr) {
    if (!dateStr) return '-';
    const d = new Date(dateStr);
    const pad = n => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

function formatShortDate(dateStr) {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    const pad = n => String(n).padStart(2, '0');
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`;
}

const CouponType = { FULL_REDUCTION: '满减', DISCOUNT: '折扣', NO_THRESHOLD: '无门槛' };
const CouponStatus = { UNUSED: '未使用', PENDING_VERIFICATION: '待核销', VERIFIED: '已核销', EXPIRED: '已过期' };
const MemberStatus = { NORMAL: '正常', DISABLED: '禁用' };
