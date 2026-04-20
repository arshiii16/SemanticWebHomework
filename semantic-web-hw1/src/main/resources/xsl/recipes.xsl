<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="userSkill"/>

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Recipes Displayed with XSL</title>
                <style>
                    body {
                    font-family: Arial, sans-serif;
                    margin: 30px;
                    background-color: #f7f7f7;
                    }

                    h1 {
                    text-align: center;
                    }

                    .recipe-card {
                    border: 1px solid #ccc;
                    border-radius: 8px;
                    padding: 15px;
                    margin-bottom: 20px;
                    }

                    .user-box {
                    background-color: white;
                    border: 1px solid #ccc;
                    border-radius: 8px;
                    padding: 15px;
                    margin-bottom: 20px;
                    }

                    .nav {
                    text-align: center;
                    margin-bottom: 20px;
                    }

                    .nav a {
                    padding: 10px 16px;
                    background: #222;
                    color: white;
                    text-decoration: none;
                    border-radius: 6px;
                    }
                </style>
            </head>
            <body>
                <h1>Recipe List Rendered with XSL</h1>

                <div class="nav">
                    <a href="/recipes">Back to recipes</a>
                </div>

                <div class="user-box">
                    <p>
                        <strong>Current skill used for highlighting:</strong>
                        <xsl:value-of select="$userSkill"/>
                    </p>
                    <p><strong>Yellow</strong> = matches user skill</p>
                    <p><strong>Green</strong> = does not match user skill</p>
                </div>

                <xsl:for-each select="recipes/recipe">
                    <div class="recipe-card">
                        <xsl:attribute name="style">
                            border:1px solid #ccc;
                            border-radius:8px;
                            padding:15px;
                            margin-bottom:20px;
                            background-color:
                            <xsl:choose>
                                <xsl:when test="difficultyLevels/level = $userSkill">yellow</xsl:when>
                                <xsl:otherwise>lightgreen</xsl:otherwise>
                            </xsl:choose>;
                        </xsl:attribute>

                        <h2>
                            <xsl:value-of select="title"/>
                        </h2>

                        <p>
                            <strong>ID:</strong>
                            <xsl:value-of select="@id"/>
                        </p>

                        <p>
                            <strong>Cuisine Types:</strong>
                            <xsl:for-each select="cuisineTypes/cuisine">
                                <xsl:value-of select="."/>
                                <xsl:if test="position() != last()">, </xsl:if>
                            </xsl:for-each>
                        </p>

                        <p>
                            <strong>Difficulty Levels:</strong>
                            <xsl:for-each select="difficultyLevels/level">
                                <xsl:value-of select="."/>
                                <xsl:if test="position() != last()">, </xsl:if>
                            </xsl:for-each>
                        </p>

                        <p>
                            <strong>Description:</strong>
                            <xsl:value-of select="description"/>
                        </p>
                    </div>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>