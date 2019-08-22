VERSION_STR=$(git tag --sort=-creatordate | head -1)
if  [[ $VERSION_STR = *[!\ ]* ]]; then
	echo $VERSION_STR
fi
