#echo "::set-output name=release_tag::$(date +"%Y")"
if [ $(echo $(date +"%m-%d")) = "01-01" ]; then
  {
   touch release.txt
   echo "打包$(($(echo $(date +"%Y"))-1))年图片。" >> release.txt
  }
   else
     {
     touch release.txt
     echo "打包$(echo $(date +"%Y"))-$((10#$(echo $(date +"%m")) - 1))月份的图片。" >> release.txt
     }
fi
#echo "::set-output name=release_tag::v$(echo $(date +"%Y")).$((10#$(echo $(date +"%m")) - 1))"
echo "release_tag=v$(echo $(date +"%Y")).$((10#$(echo $(date +"%m")) - 1))" >> $GITHUB_OUTPUT
