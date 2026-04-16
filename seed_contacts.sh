#!/bin/bash

contacts=(
  "Alice Martin|+33612345678"
  "Bob Dupont|+33698765432"
  "Camille Leroy|+33645231789"
  "David Moreau|+33678901234"
  "Emma Bernard|+33654321098"
  "Farida Haddad|+21261234567"
  "Gabriel Silva|+55119876543"
  "Hana Youssef|+21269876543"
  "Ibrahim Khalil|+96512345678"
  "Julia Petit|+33611223344"
)

for entry in "${contacts[@]}"; do
  NAME="${entry%%|*}"
  PHONE="${entry##*|}"
  adb shell am start -a android.intent.action.INSERT \
    -t vnd.android.cursor.dir/contact \
    -e name "$NAME" \
    -e phone "$PHONE" \
    --activity-clear-top \
    > /dev/null 2>&1
  sleep 1
  adb shell input keyevent 4
  sleep 0.5
done

echo "Done — 10 contacts seeded."
