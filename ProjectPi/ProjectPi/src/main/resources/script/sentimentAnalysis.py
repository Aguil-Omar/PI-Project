from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer
import sys
import json

def analyze_sentiment(text):
    analyzer = SentimentIntensityAnalyzer()
    sentiment_score = analyzer.polarity_scores(text)

    # Determine sentiment label
    if sentiment_score['compound'] >= 0.05:
        sentiment = "positive"
    elif sentiment_score['compound'] <= -0.05:
        sentiment = "negative"
    else:
        sentiment = "neutral"

    # Return sentiment result in JSON format
    print(json.dumps({"sentiment": sentiment}))

if __name__ == "__main__":
    text = sys.argv[1]  # Read comment from Java
    analyze_sentiment(text)
