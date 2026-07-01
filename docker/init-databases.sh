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
echo "🗄️  PostgreSQL Çox-Veritabanı"
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

  # DB mövcudluğunu yoxla, sonra yarat
  db_exists=$(psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="postgres" -tAc "SELECT 1 FROM pg_database WHERE datname='$db';")

  if [ "$db_exists" = "1" ]; then
    echo "   ℹ️  Info: '$db' artıq mövcuddur (skip)"
  else
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="postgres" <<-EOSQL
      CREATE DATABASE "$db";
      ALTER DATABASE "$db" OWNER TO "$POSTGRES_USER";
EOSQL
    echo "   ✅ Uğurlu: '$db'"
  fi
done

echo ""
echo "=========================================="
echo "✅ Bütün veritabanları hazır!"
echo "=========================================="
echo "Mövcud veritabanlar:"
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="postgres" -t -c "SELECT datname FROM pg_database WHERE datistemplate = false ORDER BY datname;" | sed 's/^/   /'
echo ""

