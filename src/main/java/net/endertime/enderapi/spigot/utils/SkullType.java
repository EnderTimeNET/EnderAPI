package net.endertime.enderapi.spigot.utils;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public enum SkullType {

    MHF_ArrowUp(
            "eyJ0aW1lc3RhbXAiOjE1MjUxOTg4MTcwNzIsInByb2ZpbGVJZCI6ImZlZj"
                    + "AzOWVmZTZjZDQ5ODc5Yzg0MjZhM2U2MTM0Mjc3IiwicHJvZmlsZU5hbWU"
                    + "iOiJNSEZfQXJyb3dVcCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0Z"
                    + "Xh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5l"
                    + "Y3JhZnQubmV0L3RleHR1cmUvZDQ4Yjc2OGM2MjM0MzJkZmIyNTlmYjNjMzk3O"
                    + "GU5OGRlYzExMWY3OWRiZDZjZDg4ZjIxMTU1Mzc0YjcwYjNjIn19fQ==",
            "MHF_ArrowUp"), MHF_ArrowDown(
            "eyJ0aW1lc3RhbXAiOjE1MjUxOTg4NDgyMTQsInByb2ZpbGVJZCI6IjY"
                    + "4ZjU5YjliNWIwYjRiMDVhOWYyZTFkMTQwNWFhMzQ4IiwicHJvZmlsZU5hbWU"
                    + "iOiJNSEZfQXJyb3dEb3duIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRle"
                    + "HR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmF"
                    + "mdC5uZXQvdGV4dHVyZS8yZGFkZDc1NWQwODUzNzM1MmJmN2E5M2UzYmI3ZGQ0Z"
                    + "DczMzEyMWQzOWYyZmI2NzA3M2NkNDcxZjU2MTE5NGRkIn19fQ==",
            "MHF_ArrowDown"), MHF_ArrowLeft(
            "eyJ0aW1lc3RhbXAiOjE1MjUxOTg4NzI3MzcsInByb2ZpbGVJZCI6ImE2OGYw"
                    + "YjY0OGQxNDQwMDBhOTVmNGI5YmExNGY4ZGY5IiwicHJvZmlsZU5hbWUiOiJNSEZfQ"
                    + "XJyb3dMZWZ0Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlN"
                    + "LSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZ"
                    + "S8zZWJmOTA3NDk0YTkzNWU5NTViZmNhZGFiODFiZWFmYjkwZmI5YmU0OWM3MDI2YmE5"
                    + "N2Q3OThkNWYxYTIzIn19fQ==",
            "MHF_ArrowLeft"), MHF_ArrowRight(
            "eyJ0aW1lc3RhbXAiOjE1MjUxOTg4OTQxODcsInByb2ZpbGVJZCI6IjUwYzg1"
                    + "MTBiNWVhMDRkNjBiZTlhN2Q1NDJkNmNkMTU2IiwicHJvZmlsZU5hbWUiOiJNSEZfQX"
                    + "Jyb3dSaWdodCIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1c"
                    + "mVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xYjZmMWEyNWI2YmMxOTk5NDY0NzJhZWRiM"
                    + "zcwNTIyNTg0ZmY2ZjRlODMyMjFlNTk0NmJkMmU0MWI1Y2ExM2IifX19",
            "MHF_ArrowRight"), MHF_Exclamation(
            "eyJ0aW1lc3RhbXAiOjE1MjUxOTg5MjgzNzMsInByb2ZpbGVJZCI6ImQzYzQ3Zj"
                    + "ZmYWUzYTQ1YzFhZDdjZTJjNzYyYjAzYWU2IiwicHJvZmlsZU5hbWUiOiJNSEZfRXhjbG"
                    + "FtYXRpb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTi"
                    + "I6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZhNT"
                    + "NiZGQxNTQ1NTMxYzllYmI5YzZmODk1YmM1NzYwMTJmNjE4MjBlNmY0ODk4ODU5ODhhN2U4"
                    + "NzA5YTNmNDgifX19",
            "MHF_Exclamation"), MHF_Question(
            "eyJ0aW1lc3RhbXAiOjE1MjUxOTg3NzQyMzgsInByb2ZpbGVJZCI6IjYwNmUyZmYwZWQ"
                    + "3NzQ4NDI5ZDZjZTFkMzMyMWM3ODM4IiwicHJvZmlsZU5hbWUiOiJNSEZfUXVlc3Rpb24i"
                    + "LCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiO"
                    + "iJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzUxNjNkYWZhYzFkOT"
                    + "FhOGM5MWRiNTc2Y2FhYzc4NDMzNjc5MWE2ZTE4ZDhmN2Y2Mjc3OGZjNDdiZjE0NmI2In19fQ==",
            "MHF_Question"), MHF_Chest(
            "eyJ0aW1lc3RhbXAiOjE1MjUxOTg5NTE3OTgsInByb2ZpbGVJZCI6IjczZDRlMDY4M2E2ZD"
                    + "RjOGI4Zjg1MzMyMzU0Njk1NWM0IiwicHJvZmlsZU5hbWUiOiJNSEZfQ2hlc3QiLCJzaWdu"
                    + "YXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi"
                    + "8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZmNjhkNTA5YjVkMTY2OWI5NzFk"
                    + "ZDFkNGRmMmU0N2UxOWJjYjFiMzNiZjFhN2ZmMWRkYTI5YmZjNmY5ZWJmIn19fQ==",
            "MHF_Chest"), GLOBUS(
            "eyJ0aW1lc3RhbXAiOjE1MjUxOTg5NzU5NjIsInByb2ZpbGVJZCI6IjA2NDVhOTE2YTcxNzQ5Z"
                    + "TdhMjZjYzcxYmZlNDY5YjhiIiwicHJvZmlsZU5hbWUiOiJNb2VjaHRlZ2VybkdhbWVyIiwi"
                    + "c2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR"
                    + "0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84ODJhMzU1YTJkN2E4YjkwZm"
                    + "JlMzM4YmRjYjI5ODZlZWJiMmQ2NGRmYTVhNDFlODk0YTFmM2U0OGE1NzgifX19",
            "Globus"), CommandBlock(
            "eyJ0aW1lc3RhbXAiOjE1MjUxOTkwMjY1NzgsInByb2ZpbGVJZCI6IjQ0MmM4NWY2M2UzY"
                    + "jQ5ZWQ5NDMyMDA2MTFjY2VhMzdkIiwicHJvZmlsZU5hbWUiOiJFdnRlbWEzIiwic2lnbmF0d"
                    + "XJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3Rle"
                    + "HR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84YzlmMzZiZjllODk3MzQ3OGI3NWZlNzllN"
                    + "DVjYTYyM2U4ZDljYzQ5MGFiNDc1ZGM0OTAyOGVmMjk0YTM0NTAiLCJtZXRhZGF0YSI6eyJt"
                    + "b2RlbCI6InNsaW0ifX19fQ==",
            "CommandBlock"), MHF_TNT(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDU5MjI2MTcsInByb2ZpbGVJZCI6ImQ0M2FmOTNjYzMzMDRh"
                    + "M2RiYWI4ZWU3NDIzNGEwMTFhIiwicHJvZmlsZU5hbWUiOiJNSEZfVE5UIiwic2lnbmF0d"
                    + "XJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3"
                    + "RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mOTI0MDhmZThkMGEzZWY1NTMxMDY"
                    + "1ZTlmNTY2YzMxYWE2ZWIzNzQ4NDAzMWE0NmU0NDY2NjE1ZGFmNjRmNzA1In19fQ==",
            "MHF_TNT"), MHF_TNT2(
            "eyJ0aW1lc3RhbXAiOjE1MjU5NTg0ODgzMjgsInByb2ZpbGVJZCI6IjU1ZTczMzgwYTk3Mz"
                    + "RhNTI5YmI1MWVmYTUyNTYxMjVjIiwicHJvZmlsZU5hbWUiOiJNSEZfVE5UMiIsInNpZ25"
                    + "hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA"
                    + "6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWI5OTRiNDFmMDdmODdiMzI"
                    + "4MTg2YWNmY2JkYWJjNjk5ZDViMTg0N2ZhYmIyZTQ5ZDVhYmMyNzg2NTE0M2E0ZSJ9fX0=",
            "MHF_TNT2"), Enderchest(
            "eyJ0aW1lc3RhbXAiOjE1MjU5NTg2Mzg5NDIsInByb2ZpbGVJZCI6IjU1ZWI0NGY4ZjIz"
                    + "NjRlNTBiMTU3ZTExMTNlMWE5N2M4IiwicHJvZmlsZU5hbWUiOiJUb3JpYXNfRGF4Iiwic"
                    + "2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaH"
                    + "R0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kZGNjMTg5NjMzYzc4OWNi"
                    + "NmQ1ZTc4ZDEzYTUwNDNiMjZlN2I0MGNkYjdjZmM0ZTIzYWEyMjc5NTc0OTY3YjQifX19",
            "Enderchest"), MHF_Blaze(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDU5NjcxNjUsInByb2ZpbGVJZCI6IjRjMzhlZDExNTk2YT"
                    + "RmZDRhYjFkMjZmMzg2YzFjYmFjIiwicHJvZmlsZU5hbWUiOiJNSEZfQmxhemUiLCJzaWdu"
                    + "YXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi"
                    + "8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2QwNmUzNDJmOTBlYzUzOGFhYTE1"
                    + "NTJiMjI0ZjI2NGEwNDA4NDA5MDJlMTI2ZDkxZWNlNjEzOWFhNWIzYzdjYzMifX19",
            "MHF_Blaze"), MHF_CaveSpider(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDU5OTA4NzAsInByb2ZpbGVJZCI6ImNhYjI4Nzc"
                    + "xZjBjZDRmZTdiMTI5MDJjNjllYmE3OWE1IiwicHJvZmlsZU5hbWUiOiJNSEZfQ2F2ZV"
                    + "NwaWRlciIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOI"
                    + "jp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzd"
                    + "iMDcwNjNhNjg3NGZhM2UyMjU0OGUwMjA2MmJkNzMzYzI1ODg1OTI5ODA5NjI0MTgw"
                    + "YWViYjg1MTU1N2Y2YSJ9fX0=",
            "MHF_CaveSpider"), MHF_Chicken(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYwMTYwNTEsInByb2ZpbGVJZCI6IjkyZGVhZmE5N"
                    + "DMwNzQyZDliMDAzODg2MDE1OThkNmMwIiwicHJvZmlsZU5hbWUiOiJNSEZfQ2hpY2tl"
                    + "biIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InV"
                    + "ybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTE2YjhlOT"
                    + "gzODljNTQxYmIzNjQ1Mzg1MGJjYmQxZjdiYzVhNTdkYTYyZGNjNTA1MDYwNDA5NzM3Z"
                    + "WM1YjcyYSJ9fX0=",
            "MHF_Chicken"), MHF_Cow(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYwMzg1NjIsInByb2ZpbGVJZCI6ImYxNTliMjc0YzIyZT"
                    + "QzNDBiN2MxNTJhYmRlMTQ3NzEzIiwicHJvZmlsZU5hbWUiOiJNSEZfQ293Iiwic2lnbm"
                    + "F0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cD"
                    + "ovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kMGU0ZTZmYmY1ZjNkY2Y5ND"
                    + "QyMmExZjMxOTQ0OGYxNTIzNjlkMTc5ZGJmYmNkZjAwZTViZmU4NDk1ZmE5NzcifX19",
            "MHF_Cow"), MHF_Creeper(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYwNjAzMTgsInByb2ZpbGVJZCI6IjA1N2IxYzQ3MT"
                    + "MyMTQ4NjNhNmZlODg4N2Y5ZWMyNjVmIiwicHJvZmlsZU5hbWUiOiJNSEZfQ3JlZXBlci"
                    + "IsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybC"
                    + "I6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmE1ZTk1NzM1YT"
                    + "NmMzc3MmIxYjQ4NWUxNTAyODA3YWUzOTZhNzJjNjFiZmQzNmFiNDFmYTcxYmVjMmY2NGF"
                    + "hMiJ9fX0=",
            "MHF_Creeper"), MHF_Enderman(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYwODA1OTMsInByb2ZpbGVJZCI6IjQwZmZiMzcyMTJ"
                    + "mNjQ2NzhiM2YyMjE3NmJmNTZkZDRiIiwicHJvZmlsZU5hbWUiOiJNSEZfRW5kZXJtYW4iL"
                    + "CJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJ"
                    + "odHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFiMDlhMzc1MjUxMGU5M"
                    + "TRiMGJkYzkwOTZiMzkyYmIzNTlmN2E4ZThhOTU2NmEwMmU3ZjY2ZmFmZjhkNmY4OWUifX19",
            "MHF_Enderman"), MHF_Ghast(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYxMDQxMTksInByb2ZpbGVJZCI6IjA2MzA4NWE2Nzk3ZjQ"
                    + "3ODViZTFhMjFjZDc1ODBmNzUyIiwicHJvZmlsZU5hbWUiOiJNSEZfR2hhc3QiLCJzaWduYX"
                    + "R1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vd"
                    + "GV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRhNGU0MmViMTVhMDg4MTNhNmE2ZjYx"
                    + "ZjEwYWEyODgwMTlmYTBmYWUxMDZhMjk1M2RkYjQ2Zjc3ZWUyZDc3ZiJ9fX0=",
            "MHF_Ghast"), MHF_Golem(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYxMjUyMjYsInByb2ZpbGVJZCI6Ijc1N2Y5MGIyMj"
                    + "M0NDRiOGQ4ZGFjODI0MjMyZTJjZWNlIiwicHJvZmlsZU5hbWUiOiJNSEZfR29sZW0i"
                    + "LCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cm"
                    + "wiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFjNmNkNzIw"
                    + "MmMzNGU3OGYzMDczMDkwMzQ5ZjdkOTczYjI4OGFmNWU1YjczMzRkZDI0OTAxMGIzZjI"
                    + "3MDc4ZjkifX19",
            "MHF_Golem"), MHF_Herobrine(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYxNTM1NTEsInByb2ZpbGVJZCI6Ijk1ODZlNWF"
                    + "iMTU3YTQ2NThhZDgwYjA3NTUyYTljYTYzIiwicHJvZmlsZU5hbWUiOiJNSEZfSGVyb2"
                    + "JyaW5lIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iO"
                    + "nsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yYzY1"
                    + "ZWQyODI5YzgzZTExOWE4MGRmYjIyMjE2NDQzZTg3OGVmMTA2NDljNGEzNTRmNzRiZjQ"
                    + "1YWQwNmJjMWE3In19fQ==",
            "MHF_Herobrine"), MHF_LavaSlime(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYxOTA3MDIsInByb2ZpbGVJZCI6IjA5NzJiZGQx"
                    + "NGI4NjQ5ZmI5ZWNjYTM1M2Y4NDkxYTUxIiwicHJvZmlsZU5hbWUiOiJNSEZfTGF2YVNsa"
                    + "W1lIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidX"
                    + "JsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9kOTBkNjFlOGN"
                    + "lOTUxMWEwYTJiNWVhMjc0MmNiMWVmMzYxMzEzODBlZDQxMjllMWIxNjNjZThmZjAwMGRl"
                    + "OGVhIn19fQ==",
            "MHF_LavaSlime"), MHF_MushroomCow(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYyMTE5MjEsInByb2ZpbGVJZCI6ImE0NjgxN2Q2"
                    + "NzNjNTRmM2ZiNzEyYWY2YjNmZjQ3Yjk2IiwicHJvZmlsZU5hbWUiOiJNSEZfTXVzaHJvb2"
                    + "1Db3ciLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1"
                    + "cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzEyM2NmYzU1O"
                    + "DI0NTRmY2Y5OTA2Zjg0MWZkYTJjYzZhZTg5NmNmNDU1ODIxYzRhZGExOTk4ZGU3MDg3N2N"
                    + "jODYifX19",
            "MHF_Ocelot"), MHF_Ocelot(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYyMzQ2MjEsInByb2ZpbGVJZCI6IjFiZWU5ZGY1NGY3M"
                    + "TQyYTJiZjUyZDk3OTcwZDNmZWEzIiwicHJvZmlsZU5hbWUiOiJNSEZfT2NlbG90Iiwic2l"
                    + "nbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0c"
                    + "DovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xMThiNmI3OTc4MzM2OGRmZTA"
                    + "wNDI5ODUxMTBkYTM2NmY5Yzc4OGI0NTA5N2EzZWE2ZDBkOWE3NTNlOWY0MmM2In19fQ==",
            "MHF_Ocelot"), MHF_Pig(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYyNTY4MjIsInByb2ZpbGVJZCI6IjhiNTcwNzhiZjFiZDQ1"
                    + "ZGY4M2M0ZDg4ZDE2NzY4ZmJlIiwicHJvZmlsZU5hbWUiOiJNSEZfUGlnIiwic2lnbmF0dX"
                    + "JlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3Rl"
                    + "eHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9hNTYyYTM3Yjg3MWY5NjRiZmMzZTEzMTF"
                    + "lYTY3MmFhYTAzOTg0YTVkYzQ3MjE1NGEzNGRjMjVhZjE1N2UzODJiIn19fQ==",
            "MHF_Pig"), MHF_PigZombie(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYyNzYzMzYsInByb2ZpbGVJZCI6IjE4YTJiYjUwMz"
                    + "M0YTQwODQ5MTg0MmMzODAyNTFhMjRiIiwicHJvZmlsZU5hbWUiOiJNSEZfUGlnWm9tYmllI"
                    + "iwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoi"
                    + "aHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85MTZkMTY3YzU3NDRlZDE"
                    + "0ZWJjMDJmNDQ3ZjMyNjE0MDU5MzYyYjdkMmVjYjgwOGZmMDYxNjVkMmMzNDNiZWYyIn19fQ==",
            "MHF_PigZombie"), MHF_Sheep(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYyOTg3MzUsInByb2ZpbGVJZCI6ImRmYWFkNTUxNGU3ZTQ1"
                    + "YTFhNmY3YzZmYzVlYzgyM2FjIiwicHJvZmlsZU5hbWUiOiJNSEZfU2hlZXAiLCJzaWduYXR1c"
                    + "mVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4d"
                    + "HVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdjYTM4Y2NmNDE3ZTk5Y2E5ZDQ3ZWViMTVhO"
                    + "GEzMGVkYjE1MDdhYTUyYjY3OGMyMjBjNzE3YzQ3NGFhNmZlM2UifX19",
            "MHF_Sheep"), MHF_Skeleton(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYzMjA0OTcsInByb2ZpbGVJZCI6ImEzZjQyN2E4MThjNT"
                    + "Q5YzVhNGZiNjRjNmUwZTFlMGE4IiwicHJvZmlsZU5hbWUiOiJNSEZfU2tlbGV0b24iLCJzaWd"
                    + "uYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8v"
                    + "dGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzIyNzk1YzNjNmYzNmQ2N2RlY2Y5YTMxO"
                    + "TVlMTI4MDQwYmVjNTIyNmIwNTVmMmIxNmQ0NmZhMTlhOTE4MGUwMjMifX19",
            "MHF_Skeleton"), MHF_Slime(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY2NTcyMDYsInByb2ZpbGVJZCI6Ijg3MGFiYTkzNDBlODQ4Y"
                    + "jM4OWM1MzJlY2UwMGQ2NjMwIiwicHJvZmlsZU5hbWUiOiJNSEZfU2xpbWUiLCJzaWduYXR1cm"
                    + "VSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHV"
                    + "yZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg2YzI3YjAxM2YxYmYzMzQ0ODY5ZTgxZTVjNjEw"
                    + "MDI3YmM0NWVjNWI3OTUxNGZkYzk2ZTAxZGYxYjdlM2EzODcifX19",
            "MHF_Slime"), MHF_Spider(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY2MzUyODUsInByb2ZpbGVJZCI6IjVhZDU1ZjM0NDFiNjRi"
                    + "ZDI5YzMyMTg5ODNjNjM1OTM2IiwicHJvZmlsZU5hbWUiOiJNSEZfU3BpZGVyIiwic2lnbmF0d"
                    + "XJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleH"
                    + "R1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mNjFhNDk1NDFhODM2YWE4ZjRmNzZlMGQ0Y2I"
                    + "yZmYwNDg4OGM2MmY5NDExZWExMGNiYWNmMWYyYTU0NDI0MjQwIn19fQ==",
            "MHF_Spider"), MHF_Squid(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY2MTUxNTcsInByb2ZpbGVJZCI6IjcyZTY0NjgzZTMxMzRjM"
                    + "zZhNDA4YzY2YjY0ZTk0YWY1IiwicHJvZmlsZU5hbWUiOiJNSEZfU3F1aWQiLCJzaWduYXR1cmV"
                    + "SZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZX"
                    + "MubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVlODkxMDFkNWNjNzRhYTQ1ODAyMWEwNjBmNjI4OWE1"
                    + "MWEzNWE3ZDM0ZDhjYWRkZmMzY2RmM2IyYzlhMDcxYSJ9fX0=",
            "MHF_Squid"), MHF_Villager(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY1NTQ0NTUsInByb2ZpbGVJZCI6ImJkNDgyNzM5NzY3YzQ"
                    + "1ZGNhMWY4YzMzYzQwNTMwOTUyIiwicHJvZmlsZU5hbWUiOiJNSEZfVmlsbGFnZXIiLCJzaWduY"
                    + "XR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV"
                    + "4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2I0YmQ4MzI4MTNhYzM4ZTY4NjQ4OTM4ZDdhM"
                    + "zJmNmJhMjk4MDFhYWYzMTc0MDQzNjdmMjE0Yjc4YjRkNDc1NGMifX19",
            "MHF_Villager"), MHF_WSkeleton(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY1MzU4MjUsInByb2ZpbGVJZCI6IjdlZDU3MWE1OWZiOD"
                    + "QxNmM4YjlkZmIyZjQ0NmFiNWIyIiwicHJvZmlsZU5hbWUiOiJNSEZfV1NrZWxldG9uIiwic2ln"
                    + "bmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3"
                    + "RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iYTk2ZTlkNzZiZWQzMDA5MGNlNmUyZDg0"
                    + "MjU5OTY1OTRlZWM2ZDY4YWM4OGNmMDczNTZlOTgxNDgzNDI0M2VjIn19fQ==",
            "MHF_WSkeleton"), MHF_Zombie(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY1MTU0MDQsInByb2ZpbGVJZCI6ImRhY2EyYzNkNzE5YjQxZjV"
                    + "iNjI0ZTQwMzllNmMwNGJkIiwicHJvZmlsZU5hbWUiOiJNSEZfWm9tYmllIiwic2lnbmF0dXJlUmVx"
                    + "dWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1p"
                    + "bmVjcmFmdC5uZXQvdGV4dHVyZS9kOTdlNDI1OTM3OWEwNmYyNDg0M2MxYmI0MmYyZGYzNWMxM2Y4"
                    + "MDFhZDA3OWY3MTViZGVkNDg4ZGI4ZjU3YzMifX19",
            "MHF_Zombie"), MHF_Cactus(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY0OTYwODYsInByb2ZpbGVJZCI6IjFkOTA0OGRiZTgzNjRiOWF"
                    + "hMTA4NTUwMTQ5MjJmMWFlIiwicHJvZmlsZU5hbWUiOiJNSEZfQ2FjdHVzIiwic2lnbmF0dXJlUmVx"
                    + "dWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pb"
                    + "mVjcmFmdC5uZXQvdGV4dHVyZS80MGQ0YzhlOTBlZGVmOGNkZTJiODljN2IxYzY1YmEwMmVjMzM0MWJ"
                    + "hZDExNWUwMGRlMjE5OTEwNWZmMTczZDk0In19fQ==",
            "MHF_Cactus"), MHF_Cake(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY0Nzc4MDAsInByb2ZpbGVJZCI6ImFmYjQ4OWM0OWZjODQ4YTQ5OG"
                    + "YyZGQ3YmVhNDE0YzlhIiwicHJvZmlsZU5hbWUiOiJNSEZfQ2FrZSIsInNpZ25hdHVyZVJlcXVpcmV"
                    + "kIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3Jh"
                    + "ZnQubmV0L3RleHR1cmUvZWMyNDFhNTk3YzI4NWUxMDRjMjcxMTk2ZDc4NWRiNGNkMDExMGE0MGM4Zj"
                    + "hlNWQzNTRjNTY0NDE1OTU2N2M5ZCJ9fX0=",
            "MHF_Cake"), MHF_CoconutB(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY0NjA5MTcsInByb2ZpbGVJZCI6IjYyZWZhOTczZjYyNjQwOT"
                    + "JhZWRlNTdmZmJlODRmZjJiIiwicHJvZmlsZU5hbWUiOiJNSEZfQ29jb251dEIiLCJzaWduYXR1cmV"
                    + "SZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMu"
                    + "bWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdiM2ZkN2QzNzViZGU1MTgyOGM2ZjZlYzg4OGIyZWE4NmYyM"
                    + "2ZhMzQ4ZGI3YmRkZTAzZjdlMDA3YzNjMjQwMGIifX19",
            "MHF_CoconutB"), MHF_CoconutG(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY0NDEyMTksInByb2ZpbGVJZCI6Ijc0NTU2ZmVhMjhlZDQ0NTg"
                    + "4ZGIyOWE4MjIwZGEwYzEyIiwicHJvZmlsZU5hbWUiOiJNSEZfQ29jb251dEciLCJzaWduYXR1cmVSZ"
                    + "XF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWl"
                    + "uZWNyYWZ0Lm5ldC90ZXh0dXJlL2JhZmI4MzRmYWM5ZDFmZjJhODA2MWNlOTE3YjUwZWI4OWNlZDkzM"
                    + "mZkZjUwZTM3Yjk3MWM3NWJjOGQxMzlmOGEifX19",
            "MHF_CoconutG"), MHF_Melon(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDY0MjIwOTgsInByb2ZpbGVJZCI6IjFjN2Q5Nzg0NDdlYTRiZjNi"
                    + "YzIzYWNmMjYwYjQzNmU2IiwicHJvZmlsZU5hbWUiOiJNSEZfTWVsb24iLCJzaWduYXR1cmVSZXF1a"
                    + "XJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZW"
                    + "NyYWZ0Lm5ldC90ZXh0dXJlLzgwOTJkNzYwYzQ1MzYyNTU5NDY2MmM5ZmM4NjgxNTJhMDFhMWY2Zjhk"
                    + "NjEzN2ZiODY4ZGE1YTk1YmJkMWY1OCJ9fX0=",
            "MHF_Melon"), MHF_OakLog(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYzOTkwNTAsInByb2ZpbGVJZCI6ImUyMjRlNWVjZTI5OTQwMDVhZ"
                    + "TIyM2IwZjc3YTU3NzE0IiwicHJvZmlsZU5hbWUiOiJNSEZfT2FrTG9nIiwic2lnbmF0dXJlUmVxdWl"
                    + "yZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjc"
                    + "mFmdC5uZXQvdGV4dHVyZS9lYmUxZGEwMWYxNzRkNDYyMzkxNjZkYzVmYjRhMGFmODhiOWVlMzhiN2R"
                    + "hOTE4ZjMzZDgzOGJjYTg1YmE3ZjNjIn19fQ==",
            "MHF_OakLog"), MHF_Present1(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYzNzg5NDQsInByb2ZpbGVJZCI6IjE1NmIyNTFiMTJlMDQ4Mjlh"
                    + "MTMwYTYxYjUzYmE3NzIwIiwicHJvZmlsZU5hbWUiOiJNSEZfUHJlc2VudDEiLCJzaWduYXR1cmVSZXF"
                    + "1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZW"
                    + "NyYWZ0Lm5ldC90ZXh0dXJlLzJlMTBhYzk5OTcwM2M3MzViZjdiYTZkYTYzZjk4NDJkNDE1YzViZGUwN"
                    + "2E3NDJmOTZmMzA2MGUxMWNjZmE2YzYifX19",
            "MHF_Present1"), MHF_Present2(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYzNTc3NDAsInByb2ZpbGVJZCI6ImYxZWI3Y2FkZTJjMDRlOWU4"
                    + "YWFkMWVhZTIxZDVmZDk1IiwicHJvZmlsZU5hbWUiOiJNSEZfUHJlc2VudDIiLCJzaWduYXR1cmVSZXF"
                    + "1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZW"
                    + "NyYWZ0Lm5ldC90ZXh0dXJlLzQwN2MwZDM0ZjkwNTQ4YWJmOTQwNDBlNTE2N2MxZTA3YzM0MjdiOTIwZ"
                    + "jY2NjgwZDczNTZlMjNlMTFjMWE3N2YifX19",
            "MHF_Present2"), MHF_EnderDragon(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDgyOTM0MDQsInByb2ZpbGVJZCI6ImJkMzgwMmJiYmU0ODQ"
                    + "zOGNiYWZiY2I5NTEwZTJhYTJkIiwicHJvZmlsZU5hbWUiOiJNSEZfRW5kZXJEcmFnb24iLCJzaWduYX"
                    + "R1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZ"
                    + "XMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2U1MjUzYjdhYzU5OTRlMjRjNzgzYTY2NzMzMzI1NzVlOWUy"
                    + "Y2E5YTM2YjA2YmQ1NWU5YTkyYzYzNTVkM2QyYTAifX19",
            "MHF_EnderDragon"), MHF_Pumpkin(
            "eyJ0aW1lc3RhbXAiOjE1MzA1NDYzMzk5NDAsInByb2ZpbGVJZCI6ImY0NGQzNTViYjZhZTRiYTg4Z"
                    + "TYyYWU2NDQxODU0Nzg1IiwicHJvZmlsZU5hbWUiOiJNSEZfUHVtcGtpbiIsInNpZ25hdHVyZVJlcXVp"
                    + "cmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3J"
                    + "hZnQubmV0L3RleHR1cmUvYjE2MmQ3MmQ4ZDU3NmQ5YmJiODFjOWFlZGM2OTA2MTc0MTZkZWRjOTVjY2"
                    + "YxN2Y3OTQ1ODBjYTY3NDk0MmE2NSJ9fX0=",
            "MHF_Pumpkin"), LAMP(
            "eyJ0aW1lc3RhbXAiOjE1Mzg2ODU0NDU1OTQsInByb2ZpbGVJZCI6IjdlNThkNGMyY2MyNTRmN2U4ODk1MjMw"
                    + "M2JiYjc1YzYxIiwicHJvZmlsZU5hbWUiOiJsYW1wIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInR"
                    + "leHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dH"
                    + "VyZS9jYjljN2JlNzdkOWE3MDhjNzc1Y2UwZWE3NjI1ZWQyYjY1MTgyZTI1MDg0M2Q0OTE1ZGNlYjd"
                    + "hZWY1MWMxNjVkIn19fQ==",
            "Lampe"), EMERLADORE(
            "eyJ0aW1lc3RhbXAiOjE1MzkxOTkwNjMyOTIsInByb2ZpbGVJZCI6IjkzYjk5ZjIzZjIxYzRiZTJi"
                    + "ZWU4YWYzZjg3NDBjNzc3IiwicHJvZmlsZU5hbWUiOiJUZXJlbmVja2xhIiwic2lnbmF0dXJlUmVx"
                    + "dWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1p"
                    + "bmVjcmFmdC5uZXQvdGV4dHVyZS8yNGMxMjdmMmVkZjkxZjllYzk2ZjkzOGFjMWIzY2ZlOWQ3YTA3"
                    + "YjM1NGI0MGQ5M2I5MzVhMDg4YzYwODJmMDNlIn19fQ==",
            "SmaragdErz"), ENDEREYE(
            "eyJ0aW1lc3RhbXAiOjE1MzkxOTkxNDI0NjgsInByb2ZpbGVJZCI6ImEwNjY0NjlmMWVhYTRhMTdhM2"
                    + "U3OWRiN2Q0MWNiYzQzIiwicHJvZmlsZU5hbWUiOiJFZG5hX0kiLCJzaWduYXR1cmVSZXF1aXJlZCI"
                    + "6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0"
                    + "Lm5ldC90ZXh0dXJlLzQwYTFjZGM1NGRiNTQ1YjdjMDczNGUzYTkyNDc3NmU4YmQwNTkxYmQ1ZDNiY"
                    + "jc1ZDM5OWY4NGJhMjkxOGQ5MzEifX19",
            "Enderauge"), JUKEBOX(
            "eyJ0aW1lc3RhbXAiOjE1MzkxOTkyNDQzNjEsInByb2ZpbGVJZCI6IjBiOGIyMjQ1ODAxODQ1NmM5NDV"
                    + "kNDI4MjEyMWUxYjFlIiwicHJvZmlsZU5hbWUiOiJDNDE4Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRy"
                    + "dWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZ"
                    + "XQvdGV4dHVyZS9lYWZjZjQ0M2I1YjUxYjczNTI2ODg5NjRkYTQ2ZmRiMzMwZDA1N2M0MDNkYzhkMm"
                    + "M2N2UzYzcxNjlhZGM2MzQ3In19fQ==",
            "Jukebox"), RADIO(
            "eyJ0aW1lc3RhbXAiOjE1MzkxOTkzMjY4MTQsInByb2ZpbGVJZCI6IjhlMzllYzNmMmFmNDQ4ZDhhZTM5Mj"
                    + "VlOWI4NTYxNTI3IiwicHJvZmlsZU5hbWUiOiJ1aW96Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUs"
                    + "InRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdG"
                    + "V4dHVyZS9kYjlmMGNlY2I3MTk0ZDkxNDc3MDdkN2E1OTQzN2FjNWVjODc3OTIxZDY1ZTkxZGVmM2RlZ"
                    + "mE5YTQ0NmFjMGI0In19fQ==",
            "Radio"), DISPENSER(
            "eyJ0aW1lc3RhbXAiOjE1MzkxOTkzOTA2NDEsInByb2ZpbGVJZCI6ImI2ZDQ0NDhhZmFlYTQyOWFhZWQ"
                    + "wZTI4YzY1NzA3MDdlIiwicHJvZmlsZU5hbWUiOiJzY2VtbSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cn"
                    + "VlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L"
                    + "3RleHR1cmUvNjBkNGZlZDc4ZjI0NmZiZWQ5YTJkOWZlMzg4N2I5YjNlMDhlNDJmZmNmYjg0MjgxOWNk"
                    + "MTcxYjNmNWQzMTkifX19",
            "Dispenser"), WHITE_CHEST(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleH" +
                    "R1cmUvNmU5NzNiNTBkMGIyZGE2ZTJhNTFlY2VlYTBkMjJkNjdhNjE3OThlOTc1OGZkZjViOTIzYjJhNTk1Yz" +
                    "YxNzYifX19",
            "White_Chest"), MHF_Witch(
            "eyJ0aW1lc3RhbXAiOjE1NDk2Mjk1MzI2MjksInByb2ZpbGVJZCI6ImZlZjg1YzQ5MmZkZjQ3Zjg5MTMy" +
                    "NTUyMDQ2MjQzMjIzIiwicHJvZmlsZU5hbWUiOiJNSEZfV2l0Y2giLCJzaWduYXR1cmVSZXF1aXJlZCI" +
                    "6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5" +
                    "ldC90ZXh0dXJlLzIwYTQ2Nzk5YjZjZTRiN2UyOWE4ZGVmOWY1NGYzMGNjNzAyNWU5NjMyMTYyNWYyYWI" +
                    "0MGE5ZDcwYjg0MzZiMjEifX19",
            "Hexe"), STONE_ARROW_DOWN(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1" +
                    "cmUvOWI3Y2U2ODNkMDg2OGFhNDM3OGFlYjYwY2FhNWVhODA1OTZiY2ZmZGFiNmI1YWYyZDEyNTk1ODM3YTg0O" +
                    "DUzIn19fQ==",
            "Stone_Arrow_Down"), STONE_ARROW_UP(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmU" +
                    "vNThmZTI1MWE0MGU0MTY3ZDM1ZDA4MWMyNzg2OWFjMTUxYWY5NmI2YmQxNmRkMjgzNGQ1ZGM3MjM1ZjQ3NzkxZC" +
                    "J9fX0=",
            "Stone_Arrow_Up"), STONE_ARROW_LEFT(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY" +
                    "mIwZjZlOGFmNDZhYzZmYWY4ODkxNDE5MWFiNjZmMjYxZDY3MjZhNzk5OWM2MzdjZjJlNDE1OWZlMWZjNDc3In19fQ==",
            "Stone_Arrow_Left"), STONE_ARROW_RIGHT(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZj" +
                    "JmM2EyZGZjZTBjM2RhYjdlZTEwZGIzODVlNTIyOWYxYTM5NTM0YThiYTI2NDYxNzhlMzdjNGZhOTNiIn19fQ==",
            "Stone_Arrow_Right"), INFORMATION(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA" +
                    "xYWZlOTczYzU0ODJmZGM3MWU2YWExMDY5ODgzM2M3OWM0MzdmMjEzMDhlYTlhMWEwOTU3NDZlYzI3NGEwZiJ9fX0=",
            "Information"), FORWARD(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGE" +
                    "xODcwMTRlMGUzNDRkYzY3MjlhYmVhMTY1Njg3OTU0OTI5MGYzMDQ4ZDNmY2I4YjJiMWZhNDIxNmYyYzA5NCJ9fX0=",
            "Forward"), WINRAR(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg3" +
                    "MDMxYzQ3MjZkZGVkZDY1YjZhMTFkMzE0N2U2NzI0ZGVmYmIyOTBkYTI5Y2JiNzlkYTI0OTA1NDZjYmYifX19",
            "Winrar"), TOPAZ_CHEST(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg2" +
                    "NTQ0Yzg5YTcyODJiODJkYWI3NjU3NWY4YTE5MTEzMmIzZDZjNjVmMTAyODNlZjBjZjkyYWI1MzViNjhmIn19fQ==",
            "Topaz_Chest"), CONCRETE_LIME(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGI1OT" +
                    "ljNjE4ZTkxNGMyNWEzN2Q2OWY1NDFhMjJiZWJiZjc1MTYxNTI2Mzc1NmYyNTYxZmFiNGNmYTM5ZSJ9fX0=",
            "Concrete_Lime"), CONCRETE_RED(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjE4N" +
                    "TZjN2IzNzhkMzUwMjYyMTQzODQzZDFmOWZiYjIxOTExYTcxOTgzYmE3YjM5YTRkNGJhNWI2NmJlZGM2In19fQ==",
            "Concrete_Red"), BACKWARD(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1" +
                  "cmUvODY1MmUyYjkzNmNhODAyNmJkMjg2NTFkN2M5ZjI4MTlkMmU5MjM2OTc3MzRkMThkZmRiMTM1NTBmOGZkYWQ1ZiJ9fX0=",
            "BackWard"), GOLDEN_F(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L" +
                    "3RleHR1cmUvNzBkYzk0MjBjMTRmY2FiOThkY2Q2ZjVhZDUxZThlYmUyYmI5Nzg5NTk3NmNhYTcwNTc4ZjczYzY2ZGZiZCJ9fX0=",
            "Golden_P"), GOLDEN_P(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc" +
                    "1Y2M1ZDQ3ZDVhYTBhNzhkMjRmZTQ5NDI4ZTNhNGI1MWM5MjE3NTQ0ZmEwN2JjNGNkNzdjMmU1Y2I5NmEifX19",
            "Golden_C"), GOLDEN_C(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2NhM" +
                    "WIzYzM4NGQyZDM2MzYwNzhkZDM4MTg0NTc5ODZmOTBjNGE3MTExOGFiOGRkZTgzZTE1YTM5ZWQ5ZmQifX19",
            "Purple_F"), PURPLE_F(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1c" +
                    "mUvYzExM2MyMGJlODg2YzM4N2ZlNTU1NGZlZmI0ZTZiYzVkZDZjYzVhMzg5OWRjYzI2Y2JlMzQ0NzQ4MWNlNiJ9fX0=",
            "Purple_P"), PURPLE_P(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmY" +
                    "zYzRiZDZmZDRlMTVkZmZhYzM0NjgwYTc2NmI4YTJiNWQ1MjM0NTFjYmQzZjk4MzQyOTRjYzhmMmM5MmJmIn19fQ==",
            "Concrete_Red"), PURPLE_C(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2" +
                    "IyMjQ0NDRiNDQ3Y2I1MmVhNjE3NjdmYTViODU0ODc0YzQ5NmNlMTNlMzk5MTI1MTNiMzg4ZDY3OTQ4NWQwIn19fQ==",
            "Purple_C"), EARTH(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM" +
                    "TI4OWQ1YjE3ODYyNmVhMjNkMGIwYzNkMmRmNWMwODVlODM3NTA1NmJmNjg1YjVlZDViYjQ3N2ZlODQ3MmQ5NCJ9fX0=",
            "Earth"), BESTFRIEND(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNz" +
                    "ZjYmFlNzI0NmNjMmM2ZTg4ODU4NzE5OGM3OTU5OTc5NjY2YjRmNWE0MDg4ZjI0ZTI2ZTA3NWYxNDBhZTZjMyJ9fX0=",
            "BestFriend"), COMMANDBLOCK_IMPULSE(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMj" +
                    "c3MTJjYTY1NTEyODcwMWVhM2U1ZjI4ZGRkNjllNmE4ZTYzYWRmMjgwNTJjNTFiMmZkNWFkYjUzOGUxIn19fQ==",
            "CommandBlock_Impulse"), PLAYER_BLOCK(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" +
                    "ZjliYTdmZWY2YTFhOGJkODk5YWJhZTRhNWI1NGNiMGVjZTUzYmFkYzY3N2MxNjY4YmVlMGE0NjIxYTgifX19",
            "Player_Block"), ENDER_BLOCK(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmU" +
                    "vMTQzYzc5Y2Q5YzJkMzE4N2VhMDMyNDVmZTIxMjhlMGQyYWJiZTc5NDUyMTRiYzU4MzRkZmE0MDNjMTM0ZTI3In19fQ==",
            "Ender_Block"), ENDERPLUS_BLOCK(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3R" +
                    "leHR1cmUvOTdjMmQ1ZWVlODRiYmExZDdlOTRmOTMzYTBhNTU2ZWQ3ZWE0ZTRmYTY1ZThlOWY1NjMyNTgxM2IifX19",
            "Enderplus_Block"), FRIEND_BLOCK(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cm" +
                    "UvZTllNGJkY2YxNzJkNWRjNzdjMmJkNGUzN2FkOTg1Mzk5YTlmMmNkZWJmNzI0NjM5MjllYTRiNjY2ZWY2ZjgwIn19fQ==",
            "Friend_Block"), YOUTUBER_BLOCK(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" +
                    "NTkzZjY3ZjlmNzMwZDQyZmRhOGRlNjk1NjVlYTU1ODkyYzVmODVkOWNhZTZkZDZmY2JhNWQyNmYxZTcyMzhkMSJ9fX0=",
            "Youtuber_Block"), PARTNER_BLOCK(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1c" +
                    "mUvZDIxNmNhYWFhYTNmNjE2NzkzZDE4ZTk0M2NkM2E0NjMzMTM0M2JiNmU3NzQyZTVjZDc4MWZmZmVkZDYwNGIifX19",
            "Partner_Block"), TEAM_BLOCK(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" +
                    "YzI1NjljYzY3ZDY3OTRiZWIwM2UyY2IyNDJiZDEwN2U0OWVlOTRjODNkNjRjMjNiNzdjNzk4YTJjZjEyMzFjIn19fQ==",
            "Team_Block"), BARRIER(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmU" +
                    "vM2VkMWFiYTczZjYzOWY0YmM0MmJkNDgxOTZjNzE1MTk3YmUyNzEyYzNiOTYyYzk3ZWJmOWU5ZWQ4ZWZhMDI1In19fQ==",
            "Barrier"), GRASS_BLOCK(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvO" +
                    "DQ0OWI5MzE4ZTMzMTU4ZTY0YTQ2YWIwZGUxMjFjM2Q0MDAwMGUzMzMyYzE1NzQ5MzJiM2M4NDlkOGZhMGRjMiJ9fX0=",
            "Grass_Block"), COMMAND_BLOCK_CHAIN(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRj" +
                    "ZjI5NjI4NzhjYmY3ZTE0NWM5MmVmY2MxYzYzYmZkMjRkYzEyNmZjNTE5ZDA4YjE4MzE4ZTRiMWE0ZSJ9fX0=",
            "Command_Block_Chain"), RED_HEART(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjgz" +
                    "N2YzZGIxM2E0MGQ0OTc5ZGU3NzE3OWUxOGFmNmUwYmMzY2MzOWVhNmFiYTUxOGJiMDgwYTZmMDFhNDAifX19", "Red_Heart");

    private ItemStack item;
    private String name;

    public static List<SkullType> list = new ArrayList<SkullType>();

    SkullType(String value, String name) {
        item = CreateSkulls.createSkull(value, name);
        this.name = name;
    }

    public ItemStack getItemStack() {
        return item;
    }

    public String getName() {
        return name;
    }

    public static void fillList() {
        list.add(MHF_ArrowUp);
        list.add(MHF_ArrowDown);
        list.add(MHF_ArrowLeft);
        list.add(MHF_ArrowRight);
        list.add(MHF_Exclamation);
        list.add(MHF_Question);
        list.add(MHF_Chest);
        list.add(GLOBUS);
        list.add(CommandBlock);
        list.add(MHF_TNT);
        list.add(MHF_TNT2);
        list.add(Enderchest);
        list.add(MHF_Blaze);
        list.add(MHF_CaveSpider);
        list.add(MHF_Cow);
        list.add(MHF_Creeper);
        list.add(MHF_Enderman);
        list.add(MHF_Ghast);
        list.add(MHF_Golem);
        list.add(MHF_Herobrine);
        list.add(MHF_LavaSlime);
        list.add(MHF_MushroomCow);
        list.add(MHF_Ocelot);
        list.add(MHF_Pig);
        list.add(MHF_PigZombie);
        list.add(MHF_Sheep);
        list.add(MHF_Skeleton);
        list.add(MHF_Slime);
        list.add(MHF_Spider);
        list.add(MHF_Squid);
        list.add(MHF_Villager);
        list.add(MHF_WSkeleton);
        list.add(MHF_Zombie);
        list.add(MHF_Cactus);
        list.add(MHF_Cake);
        list.add(MHF_CoconutB);
        list.add(MHF_CoconutG);
        list.add(MHF_Melon);
        list.add(MHF_OakLog);
        list.add(MHF_Present1);
        list.add(MHF_Present2);
        list.add(MHF_EnderDragon);
        list.add(MHF_Pumpkin);
        list.add(LAMP);
        list.add(DISPENSER);
        list.add(ENDEREYE);
        list.add(EMERLADORE);
        list.add(JUKEBOX);
        list.add(RADIO);
        list.add(WHITE_CHEST);
        list.add(MHF_Witch);
        list.add(STONE_ARROW_DOWN);
        list.add(STONE_ARROW_UP);
        list.add(STONE_ARROW_LEFT);
        list.add(STONE_ARROW_RIGHT);
        list.add(INFORMATION);
        list.add(FORWARD);
        list.add(BACKWARD);
        list.add(GOLDEN_C);
        list.add(GOLDEN_F);
        list.add(GOLDEN_P);
        list.add(PURPLE_C);
        list.add(PURPLE_F);
        list.add(PURPLE_P);
        list.add(EARTH);
        list.add(BESTFRIEND);
        list.add(COMMANDBLOCK_IMPULSE);
        list.add(PLAYER_BLOCK);
        list.add(ENDER_BLOCK);
        list.add(ENDERPLUS_BLOCK);
        list.add(FRIEND_BLOCK);
        list.add(YOUTUBER_BLOCK);
        list.add(PARTNER_BLOCK);
        list.add(TEAM_BLOCK);
        list.add(BARRIER);
        list.add(GRASS_BLOCK);
        list.add(RED_HEART);
        list.add(COMMAND_BLOCK_CHAIN);
    }

}
