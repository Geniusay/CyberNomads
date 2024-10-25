# 使用 Node.js 镜像作为基础镜像
FROM node:16 as build-stage

# 设置工作目录
WORKDIR /app

# 将项目文件复制到工作目录
COPY . .

# 使用 Nginx 镜像作为基础镜像
FROM nginx:stable-alpine as production-stage

# 将构建结果复制到 Nginx 服务器
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 暴露端口 3000
EXPOSE 9200

# 添加默认的 Nginx 配置文件
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 启动 Nginx 服务器
CMD ["nginx", "-g", "daemon off;"]
