for file in $(find -regex "\(.*\.java\|.*build\.gradle\)"); do
	sed -i -e '1 e cat header.txt' $file
done
