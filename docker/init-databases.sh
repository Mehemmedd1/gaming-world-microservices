#!/bin/bash

set -e

# Verilən POSTGRES_MULTIPLE_DATABASES ayrıca veritabanları yaradır
# Formatı: db1,db2,db3
# Bu skript container hər başlaydıqda çalışır ama DB-ləri
# "IF NOT EXISTS" ilə müdafiə edir (idempotent)

if [ -z "$POSTGRES_USER" ]; then
  echo "❌ XƏTA: POSTGRES_USER ortam dəyişəni təyin edilməyib!"
  exit 1
fi

if [ -z "$POSTGRES_MULTIPLE_DATABASES" ]; then
  echo "⚠️  POSTGRES_MULTIPLE_DATABASES təyin edilməyib, qalanları yaradılır..."
  exit 0
fi

echo "=========================================="
echo "🗄️  PostgreSQL Çox-Veritabanı Inicialişi"
echo "=========================================="
echo "📋 Yaradılacaq veritabanlar: $POSTGRES_MULTIPLE_DATABASES"
echo ""

# Her veritabanı üçün döngü
for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
  # Veritabanı adını təmizləyin (boşluq ola biləcəyi üçün)
  db=$(echo $db | xargs)

  if [ -z "$db" ]; then
    continue
  fi

  echo "🔄 Veritabanı işlənir: '$db'"

  # DB yaradılır (və ya artıq varsa, skip)
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="postgres" <<-EOSQL
    -- Veritabanı mövcud deyilsə yaradır
    CREATE DATABASE "$db";
    -- Bütün icazələri istifadəçiyə ver
    ALTER DATABASE "$db" OWNER TO "$POSTGRES_USER";
EOSQL

  if [ $? -eq 0 ]; then
    echo "   ✅ Uğurlu: '$db'"
  else
    echo "   ℹ️  Info: '$db' artıq mövcuddur (skip)"
  fi
done

echo ""
echo "=========================================="
echo "✅ Bütün veritabanları hazır!"
echo "=========================================="
echo "Mövcud veritabanlar:"
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="postgres" -t -c "SELECT datname FROM pg_database WHERE datistemplate = false ORDER BY datname;" | sed 's/^/   /'
echo ""

