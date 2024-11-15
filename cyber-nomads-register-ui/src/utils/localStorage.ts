const setWithExpiry = (key: string, value: any, ttl: number) => {
    const now = Date.now();
    const item = {
        value: value, // 数据值
        expiry: now + ttl, // 到期时间
    };
    localStorage.setItem(key, JSON.stringify(item));
};

const getWithExpiry = (key: string): any => {
    const itemStr = localStorage.getItem(key);
    if (!itemStr) return null;

    try {
        const item = JSON.parse(itemStr);
        const now = Date.now();

        // 检查是否过期
        if (now > item.expiry) {
            localStorage.removeItem(key); // 删除过期项
            return null;
        }

        return item.value;
    } catch (error) {
        console.error("Failed to parse stored item:", error);
        return null;
    }
};

export default {
    setWithExpiry,
    getWithExpiry
};
