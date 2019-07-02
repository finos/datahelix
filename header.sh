for file in $(find -regex "\(.*\.java\|.*build\.gradle\)"); do
	sed -i header.txt $file
done
