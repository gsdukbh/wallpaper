_date=$(echo $(date +"%Y"))
day=$(echo $(date +"%m"))
m=$(($day - 1))
if [ -$m -lt 10 ]; then
#  nl="${_date}0${m}"
  nl="${m}"
else
  nl="${_date}${m}"
fi

if [ $(echo $(date +"%m-%d")) = "01-01" ]; then
   echo "::set-output name=release_tag::$(date +"%Y")"
   touch release.txt
   echo "打包上一年图片" >> release.txt
   else
     echo "::set-output name=release_tag::${nl}"
     touch release.txt
     echo "每月打包图片" >> release.txt
fi
