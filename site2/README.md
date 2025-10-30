# Wallpaper Gallery (Hexo)

该目录存放 Hexo 版本的 GitHub Pages 源码，目标与 `site/` 内的 Jekyll 方案一致：

- 在主页突出展示最新壁纸
- 按月分组的响应式图集
- 支持历史存档及 4K 下载

## 使用方式

1. 安装依赖：
   ```bash
   cd site2
   npm install
   ```
2. 本地预览：
   ```bash
   npm run serve
   ```
3. 构建输出：
   ```bash
   npm run build
   ```
4. 部署到 `gh-pages` 分支：
   ```bash
   npm run deploy
   ```

> 构建或部署前，请确保将仓库根目录下最新的 `images/`、`archive/` 目录以及 `sqlite.db` 导出的数据复制（或软链接）至 `site2/source/`。Hexo 在构建时依赖这些静态资源与 `source/_data/wallpapers.json` 数据源。
