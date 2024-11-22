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

def format_labels(labels):
    if not labels:
        return "-"
        
    return " · ".join(label['name'] for label in labels)

def send_slack_message(blocks):
    payload = {'blocks': blocks}
    requests.post(SLACK_WEBHOOK_URL, json=payload)

prs = get_open_prs()
current_date = datetime.now().strftime("%Y년 %m월 %d일")

blocks = [
    {
        "type": "header",
        "text": {
            "type": "plain_text",
            "text": f"🔍 {current_date} 리뷰가 필요한 PR 목록",
            "emoji": True
        }
    },
    {
        "type": "divider"
    }
]

if prs:
    for pr in prs:
        labels_text = format_labels(pr['labels'])
        blocks.append({
            "type": "section",
            "text": {
                "type": "mrkdwn",
                "text": f"*<{pr['html_url']}|{pr['title']}>*\n"
                        f"작성자: {pr['user']['login']} | 작성일: {pr['created_at'][:10]}\n"
                        f"라벨: {labels_text}"
            },
            "accessory": {
                "type": "button",
                "text": {
                    "type": "plain_text",
                    "text": "리뷰하기",
                    "emoji": True
                },
                "url": pr['html_url'],
                "action_id": f"review_pr_{pr['number']}"
            }
        })
        blocks.append({"type": "divider"})
else:
    blocks.append({
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": "현재 리뷰가 필요한 PR이 없습니다. 🎉"
        }
    })

blocks.append({
    "type": "context",
    "elements": [
        {
            "type": "mrkdwn",
            "text": f"*리포지토리:* {REPO}"
        }
    ]
})

send_slack_message(blocks)
