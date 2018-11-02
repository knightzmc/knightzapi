import boto3

session = boto3.session.Session()
client = session.client('s3',
                        region_name='ams3',
                        endpoint_url='https://ams3.digitaloceanspaces.com',
                        aws_access_key_id='VWENQX4ZD2XQBDXSRBYB',
                        aws_secret_access_key='cSSP6RcGTswGxTZhveFyJ9CUuzhWLLDuHqE07b73D3k')

client.upload_file('./target/knightzapinoweb.jar',  # Path to local file
                   'knightz-plugins',  # Name of Space
                   'knightzapinoweb.jar',  # Name for remote file
                   ExtraArgs={'ACL':'public-read'}) #Access Level