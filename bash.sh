# shellcheck disable=SC2046
_date=$(echo $(date +"%Y"))
day=$(echo $(date +"%m"))
m=$(($day-1))
nl=$(echo "${_date}-${m}")
images="images/4k_${_date}-${m}"
echo "压缩所有的4k图片"
tar -czvf 4k_wallpaper.tar.gz images/4k_*
echo "压缩所有的原图图片"
tar -czvf wallpaper.tar.gz images/bing_*

if [ ! -f `$images` ]; then
  echo "开始当前压缩上个月份的图片"
  tar -czvf 4k_wallpaper`echo $(nl)`.tar.gz images/4k_`echo $(nl)`*
  tar -czvf wallpaper`echo $(nl)`.tar.gz images/bing_`echo $(nl)`*
  else
  echo "什么都不做"
fi
