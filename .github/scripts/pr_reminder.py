import os
import requests
from datetime import datetime

GITHUB_TOKEN = os.environ['GITHUB_TOKEN']
SLACK_WEBHOOK_URL = os.environ['SLACK_WEBHOOK_URL']
REPO = os.environ['GITHUB_REPOSITORY']

def get_open_prs():
    url = f"https://api.github.com/repos/{REPO}/pulls"
    headers = {'Authorization': f'token {GITHUB_TOKEN}'}
    response = requests.get(url, headers=headers)
    return response.json()

def send_slack_message(message):
    payload = {'text': message}
    requests.post(SLACK_WEBHOOK_URL, json=payload)

prs = get_open_prs()
if prs:
    message = "리뷰가 필요한 PR 목록:\n"
    for pr in prs:
        message += f"• {pr['title']} - {pr['html_url']}\n"
else:
    message = "현재 리뷰가 필요한 PR이 없습니다."

send_slack_message(message)