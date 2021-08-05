# shellcheck disable=SC2046
_date=$(echo $(date +"%Y"))
day=$(echo $(date +"%m"))
m=$(($day - 1))
if [ -$m -lt 10 ]; then
  nl=$(echo "${_date}-0${m}")
else
  nl=$(echo "${_date}-${m}")
fi
images="images/4k_$nl"
mkdir "up"
echo "压缩所有的4k图片"
tar -czf up/4k_wallpaper_all.tar.gz images/4k_*
echo "压缩完成--4k_wallpaper_all.tar.gz"
echo "压缩所有的原图图片"
tar -czf up/wallpaper_all.tar.gz images/bing_*
echo "压缩完成--wallpaper_all.tar.gz"

if [ $(echo $(date +"%m-%d")) == "01-01" ]; then
  echo "打包上一年图片"
  tar -czf up/4k_wallpaper_ $(echo $(_date)).tar.gz images/4k_$(echo $(_date))*
  tar -czf up/wallpaper_ $(echo $(_date)).tar.gz images/bing_$(echo $(_date))*
  elif [ ! -f $($images)  ]; then
    echo "开始当前压缩上个月份的图片"
    tar -czf up/4k_wallpaper_$(echo $(nl))_.tar.gz images/4k_$(echo $(nl))*
    tar -czf up/wallpaper_$(echo $(nl))_.tar.gz images/bing_$(echo $(nl))*
    else
      echo "什么都不做"
fi

