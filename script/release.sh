#echo "::set-output name=release_tag::$(date +"%Y")"
if [ $(echo $(date +"%m")) = "01" ]; then
  {
   touch release.txt
   year=$((10#$(date +"%Y") - 1))
   echo "打包 $year 年图片。" >> release.txt
   echo "release_tag=v$year" >> $GITHUB_OUTPUT
  }
   else
     {
     touch release.txt
     echo "打包$(echo $(date +"%Y"))-$((10#$(echo $(date +"%m")) - 1))月份的图片。" >> release.txt
     echo "release_tag=v$(echo $(date +"%Y")).$((10#$(echo $(date +"%m")) - 1))" >> $GITHUB_OUTPUT
     }
fi
