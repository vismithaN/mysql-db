import MySQLdb
import pandas as pd
import sys
import os

conn = MySQLdb.connect(host=os.getenv('MYSQL_DB_HOST'),
                       user=os.getenv('MYSQL_USER'),
                       passwd=os.getenv('MYSQL_PWD'),
                       db=os.getenv('MYSQL_DB'))
query = "select review_count, stars from businesses"
df = pd.read_sql(query, con=conn)
df.describe().to_csv(sys.stdout, header=False,
                     encoding='utf-8',
                     float_format="%.2f")
