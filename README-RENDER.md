# Despliegue en Render.com

## Prerrequisitos
- Cuenta en [render.com](https://render.com)
- Repositorio Git (GitHub, GitLab o Bitbucket) con el proyecto

---

## Opción A — Despliegue automático con render.yaml (recomendado)

1. Sube el proyecto a tu repositorio Git
2. En Render: **New → Blueprint**
3. Conecta tu repositorio
4. Render detecta el `render.yaml` y crea automáticamente:
   - El Web Service (app Java/Tomcat)
   - La base de datos PostgreSQL
5. Haz clic en **Apply** y espera ~5 minutos

---

## Opción B — Despliegue manual paso a paso

### 1. Crear la base de datos PostgreSQL
- **New → PostgreSQL**
- Name: `quiniela-db`
- Plan: **Basic 256MB** ($6/mes)
- Region: **Oregon**
- Clic en **Create Database**
- Anota la **Internal Database URL** que aparece en el dashboard

### 2. Crear el Web Service
- **New → Web Service**
- Conecta tu repositorio Git
- Configuración:
  - Name: `quiniela-mundial-2026`
  - Runtime: **Docker**
  - Region: **Oregon** (igual que la BD)
  - Plan: **Starter** ($7/mes)

### 3. Configurar variables de entorno
En el Web Service → **Environment**, agrega:

| Variable | Valor |
|---|---|
| `DB_DRIVER` | `org.postgresql.Driver` |
| `DB_URL` | Internal Database URL de tu BD (sin el prefijo `postgres://`, cambia a `jdbc:postgresql://...`) |
| `DB_USERNAME` | Usuario de la BD (del dashboard de Render) |
| `DB_PASSWORD` | Contraseña de la BD (del dashboard de Render) |
| `HIBERNATE_DIALECT` | `org.hibernate.dialect.PostgreSQLDialect` |
| `HIBERNATE_SHOW_SQL` | `false` |
| `HIBERNATE_DDL_AUTO` | `update` |
| `DB_POOL_MAX_SIZE` | `10` |
| `DB_POOL_MIN_IDLE` | `2` |

> ⚠️ **Importante con DB_URL**: Render entrega la URL en formato
> `postgres://user:pass@host/db` pero JDBC necesita
> `jdbc:postgresql://host/db` — ajusta el formato manualmente.

### 4. Deploy
- Haz clic en **Create Web Service**
- El primer build toma ~5-8 minutos (descarga Maven + dependencias)
- Los builds siguientes son más rápidos gracias al cache de capas Docker

---

## Verificar el despliegue

1. En el dashboard de Render, el servicio debe mostrar **Live** en verde
2. Entra a `https://tu-servicio.onrender.com`
3. Hibernate creará las tablas automáticamente (`hbm2ddl.auto=update`)
4. Registra el primer usuario y asígnale rol `ADMIN` directamente en la BD:

```sql
UPDATE usuarios SET rol = 'ADMIN' WHERE correo_electronico = 'tu@correo.com';
```

---

## Costos estimados

| Servicio | Plan | Costo |
|---|---|---|
| Web Service | Starter | $7/mes |
| PostgreSQL | Basic 256MB | $6/mes |
| **Total** | | **~$13/mes** |
