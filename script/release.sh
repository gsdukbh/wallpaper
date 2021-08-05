_date=$(echo $(date +"%Y"))
day=$(echo $(date +"%m"))
m=$(($day - 1))
yeara=$(($_date-1))
if [ -$m -lt 10 ]; then
  tagName="${_date}0${m}"
else
  tagName="${_date}${m}"
fi
#echo "::set-output name=release_tag::$(date +"%Y")"
if [ $(echo $(date +"%m-%d")) = "01-01" ]; then
  {
   touch release.txt
   echo "打包${yeara}年图片" >> release.txt
  }
   else
     {
     touch release.txt
     echo "打包${m}月份的图片" >> release.txt
     }
fi
echo "::set-output name=release_tag::${tagName})"
