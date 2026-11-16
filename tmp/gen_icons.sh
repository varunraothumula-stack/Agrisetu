#!/bin/bash
set -e
SRC="/tmp/agrisetu_master.png"

# Generate 288x288 crisp icon
convert -size 288x288 xc:'#1B5E20' \
  -fill '#2E7D32' -draw 'polygon 0,288 288,0 288,288' \
  -fill '#FFE082' -draw 'circle 144,115 144,85' \
  -fill '#81C784' -draw 'rectangle 60,195 228,212' \
  -fill '#FFFFFF' -draw 'circle 144,144 144,114' \
  -fill '#4CAF50' -draw 'circle 144,144 144,124' \
  -fill '#FFE082' -draw 'polygon 144,95 130,135 158,135' \
  -fill '#FFFFFF' -draw 'polygon 144,130 134,165 154,165' \
  "PNG32:$SRC"

BASE="app/src/main/res"
for spec in mdpi:48 hdpi:72 xhdpi:96 xxhdpi:144 xxxhdpi:192; do
  dir="$BASE/mipmap-${spec%:*}"
  size="${spec#*:}"
  mkdir -p "$dir"
  convert "$SRC" -resize "${size}x${size}!" "PNG32:$dir/ic_launcher.png"
  rm -f "$dir/ic_launcher.webp"
  radius=$((size / 2))
  convert "$SRC" -resize "${size}x${size}!" \
    \( -size "${size}x${size}" xc:none -fill white \
       -draw "circle $radius,$radius $radius,0" \) \
    -alpha set -compose DstIn -composite "PNG32:$dir/ic_launcher_round.png"
  rm -f "$dir/ic_launcher_round.webp"
done

echo "ICONS GENERATED SUCCESSFULLY"
