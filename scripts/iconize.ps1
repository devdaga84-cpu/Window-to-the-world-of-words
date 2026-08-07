# PowerShell script to create Windows .ico from source image using ImageMagick
# Usage: Edit $source and run in PowerShell where ImageMagick 'magick' command is available

$source = "window_world.png" # put your source image here (high resolution)
$iconsDir = "icons"
New-Item -ItemType Directory -Force -Path $iconsDir | Out-Null

magick "$source" -resize 1200x1200^ -gravity center -extent 1024x1024 "$iconsDir\icon_1024.png"
magick "$iconsDir\icon_1024.png" ( -size 1024x1024 xc:none -fill white -draw "roundrectangle 0,0,1023,1023,180,180" ) -alpha set -compose DstIn -composite "$iconsDir\icon_1024_rounded.png"
magick "$iconsDir\icon_1024_rounded.png" -resize 256x256 "$iconsDir\icon_256.png"
magick "$iconsDir\icon_1024_rounded.png" -resize 128x128 "$iconsDir\icon_128.png"
magick "$iconsDir\icon_1024_rounded.png" -resize 64x64 "$iconsDir\icon_64.png"
magick "$iconsDir\icon_1024_rounded.png" -resize 48x48 "$iconsDir\icon_48.png"
magick "$iconsDir\icon_1024_rounded.png" -resize 32x32 "$iconsDir\icon_32.png"
magick "$iconsDir\icon_1024_rounded.png" -resize 16x16 "$iconsDir\icon_16.png"
magick "$iconsDir\icon_256.png" "$iconsDir\icon_128.png" "$iconsDir\icon_64.png" "$iconsDir\icon_48.png" "$iconsDir\icon_32.png" "$iconsDir\icon_16.png" "app_icon.ico"
