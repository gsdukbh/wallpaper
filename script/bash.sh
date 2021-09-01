# bin/bash
_date=`date +"%Y"`
day=`date +"%m"`
m=$((10#$day - 1))
yeara=$((10#$_date-1))
if [ -$m -lt 10 ]; then
  nl=$(echo "${_date}-0${m}")
else
  nl=$(echo "${_date}-${m}")
fi


images=`ls | grep images/4k_$nl`

mkdir "up"
echo "压缩所有的4k图片"
tar -czf up/4k_wallpaper_all.tar.gz images/4k_*
echo "压缩完成--4k_wallpaper_all.tar.gz"
echo "压缩所有的原图图片"
tar -czf up/wallpaper_all.tar.gz images/bing_*
echo "压缩完成--wallpaper_all.tar.gz"

date_temp=`date +"%m-%d"`

if [ $date_temp = "01-01" ]; then
  echo "打包上一年图片"
  tar -czf up/4k_wallpaper_${yeara}.tar.gz images/4k_${yeara}*
  tar -czf up/wallpaper_${yeara}.tar.gz images/bing_${yeara}*
  elif  test -n $images ; then
    echo "开始当前压缩上个月份的图片"
    tar -czf up/4k_wallpaper_${nl}.tar.gz images/4k_${nl}*
    tar -czf up/wallpaper_${nl}.tar.gz images/bing_${nl}*
    else
      echo "什么都不做"
fi

